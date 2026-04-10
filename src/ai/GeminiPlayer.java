package ai;

import chess.PartidaXadrez;
import chess.PosicaoXadrez;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * Jogador IA usando a API do Google Gemini (modelo gemini-2.0-flash).
 * Chave de API lida da variável de ambiente GEMINI_API_KEY.
 *
 * Cooldown padrão: 2000ms entre jogadas (free tier tem limite mais restrito).
 */
public class GeminiPlayer implements AIPlayer {

    private static final String BASE_URL    = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final String NOME        = "Gemini (Flash 2.0)";
    private static final long   COOLDOWN_MS = 2000L;
    private static final int    TIMEOUT_S   = 30;
    private static final int    MAX_RETRIES = 3;

    private final String apiKey;
    private final HttpClient httpClient;

    public GeminiPlayer() throws AIAuthException {
        this.apiKey = System.getenv("GEMINI_API_KEY");
        if (this.apiKey == null || this.apiKey.isBlank()) {
            throw new AIAuthException(NOME);
        }
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_S))
                .build();
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public PosicaoXadrez[] getMove(String boardState, PartidaXadrez partida) throws AIException {
        String prompt = buildPrompt(boardState, partida, null);
        return callWithRetry(prompt, boardState, partida);
    }

    // -------------------------------------------------------------------------
    // Prompt
    // -------------------------------------------------------------------------

    private String buildPrompt(String boardState, PartidaXadrez partida, String jogadaInvalidaAnterior) {
        List<PosicaoXadrez> fontes = partida.getFontesDisponiveis();
        StringBuilder origens = new StringBuilder();
        for (PosicaoXadrez pos : fontes) {
            origens.append(pos.toString()).append(" ");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Você é um motor de xadrez. Sua única função é retornar uma jogada legal.\n\n");
        sb.append("ESTADO DO TABULEIRO (colunas a-h, linhas 1-8):\n");
        sb.append(boardState).append("\n\n");
        sb.append("INFORMAÇÕES DO TURNO:\n");
        sb.append("- Você joga com as peças: ").append(partida.getJogadorAtual()).append("\n");
        sb.append("- Número do turno: ").append(partida.getTurno()).append("\n");
        sb.append("- Está em xeque: ").append(partida.isCheck()).append("\n\n");
        sb.append("PEÇAS QUE VOCÊ PODE MOVER AGORA: ").append(origens.toString().trim()).append("\n\n");
        sb.append("REGRAS QUE VOCÊ DEVE OBEDECER:\n");
        sb.append("1. A casa de ORIGEM deve ser uma das listadas em 'PEÇAS QUE VOCÊ PODE MOVER AGORA' — nenhuma outra.\n");
        sb.append("2. A casa de DESTINO deve ser um movimento legal dessa peça.\n");
        sb.append("3. Se estiver em xeque, você OBRIGATORIAMENTE deve sair do xeque.\n");
        sb.append("4. Não capture seu próprio rei nem mova para casa ocupada pela sua cor.\n");
        sb.append("5. Peão só avança, nunca recua. Captura apenas na diagonal.\n");
        sb.append("6. Cavalo se move em L (2+1) e pode pular outras peças.\n");
        sb.append("7. Torre, Bispo e Rainha não podem passar por cima de outras peças.\n\n");

        if (jogadaInvalidaAnterior != null) {
            sb.append("ATENÇÃO: Sua tentativa anterior '").append(jogadaInvalidaAnterior)
              .append("' foi INVÁLIDA. Escolha uma jogada diferente usando apenas as origens listadas acima.\n\n");
        }

        sb.append("FORMATO OBRIGATÓRIO DA RESPOSTA:\n");
        sb.append("- Retorne SOMENTE duas coordenadas separadas por espaço: origem destino\n");
        sb.append("- Exemplo válido: e2 e4\n");
        sb.append("- Exemplo válido: g1 f3\n");
        sb.append("- NÃO escreva mais NADA. Sem explicação, sem pontuação, sem texto adicional.\n");
        sb.append("- Se escrever qualquer coisa além da jogada, sua resposta será rejeitada.");

        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // HTTP com retry contextual
    // -------------------------------------------------------------------------

    private PosicaoXadrez[] callWithRetry(String promptInicial, String boardState, PartidaXadrez partida) throws AIException {
        String ultimaJogadaInvalida = null;
        int attempt = 0;

        while (true) {
            attempt++;
            String prompt = (attempt == 1)
                    ? promptInicial
                    : buildPrompt(boardState, partida, ultimaJogadaInvalida);
            try {
                Thread.sleep(COOLDOWN_MS);
                String raw = callAPI(prompt);
                return MoveParser.parse(raw);
            } catch (AIRateLimitException e) {
                if (attempt >= MAX_RETRIES) throw e;
                System.out.println("[" + NOME + "] " + e.getMessage() + " (tentativa " + attempt + "/" + MAX_RETRIES + ")");
                sleep(e.getRetryAfterMs());
            } catch (AIBadRequestException e) {
                // jogada mal formatada — tenta de novo com feedback
                if (attempt >= MAX_RETRIES) throw e;
                ultimaJogadaInvalida = e.getMessage();
                System.out.println("[" + NOME + "] Jogada inválida, tentando novamente (" + attempt + "/" + MAX_RETRIES + ")");
            } catch (AIException e) {
                throw e;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AIException(NOME + ": interrompido durante cooldown.", e);
            }
        }
    }

    private String callAPI(String prompt) throws AIException {
        String apiUrl = BASE_URL + "?key=" + apiKey;

        String body = "{"
                + "\"contents\":[{"
                + "\"parts\":[{\"text\":\"" + escapeJson(prompt) + "\"}]"
                + "}],"
                + "\"generationConfig\":{"
                + "\"temperature\":0.2,"
                + "\"maxOutputTokens\":20"
                + "}"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(TIMEOUT_S))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (java.net.http.HttpTimeoutException e) {
            throw new AITimeoutException(NOME);
        } catch (Exception e) {
            throw new AIException(NOME + ": erro na requisição HTTP: " + e.getMessage(), e);
        }

        return handleResponse(response);
    }

    private String handleResponse(HttpResponse<String> response) throws AIException {
        int status = response.statusCode();
        String responseBody = response.body();

        switch (status) {
            case 200: return extractContent(responseBody);
            case 400: throw new AIBadRequestException(NOME, responseBody);
            case 401: case 403: throw new AIAuthException(NOME);
            case 429: {
                long retryMs = response.headers().firstValueAsLong("retry-after")
                        .map(s -> s * 1000L)
                        .orElse(8000L);
                throw new AIRateLimitException(NOME, retryMs);
            }
            case 500: case 502: case 503:
                throw new AIException("[" + NOME + "] Erro do servidor (" + status + "): " + responseBody, status);
            default:
                throw new AIException("[" + NOME + "] Resposta inesperada HTTP " + status + ": " + responseBody, status);
        }
    }

    // -------------------------------------------------------------------------
    // JSON helpers
    // -------------------------------------------------------------------------

    private String extractContent(String json) throws AIException {
        int idx = json.indexOf("\"text\":");
        if (idx == -1) throw new AIException(NOME + ": campo 'text' não encontrado na resposta: " + json);
        int start = json.indexOf('"', idx + 7) + 1;
        int end   = json.indexOf('"', start);
        if (start <= 0 || end <= start) throw new AIException(NOME + ": não foi possível extrair 'text' de: " + json);
        return json.substring(start, end);
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
