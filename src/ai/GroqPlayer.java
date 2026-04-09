package ai;

import chess.PartidaXadrez;
import chess.PosicaoXadrez;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Jogador IA usando a API da Groq (modelo llama-3.3-70b-versatile).
 * Chave de API lida da variável de ambiente GROQ_API_KEY.
 *
 * Cooldown padrão: 1500ms entre jogadas para evitar rate limit.
 */
public class GroqPlayer implements AIPlayer {

    private static final String API_URL  = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL    = "llama-3.3-70b-versatile";
    private static final String NOME     = "Groq (Llama-3.3-70b)";
    private static final long   COOLDOWN_MS = 1500L;
    private static final int    TIMEOUT_S   = 30;
    private static final int    MAX_RETRIES = 3;

    private final String apiKey;
    private final HttpClient httpClient;

    public GroqPlayer() throws AIAuthException {
        this.apiKey = System.getenv("GROQ_API_KEY");
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
        String prompt = buildPrompt(boardState, partida);
        String raw    = callWithRetry(prompt);
        return MoveParser.parse(raw);
    }

    // -------------------------------------------------------------------------
    // Prompt
    // -------------------------------------------------------------------------

    private String buildPrompt(String boardState, PartidaXadrez partida) {
        return "Você está jogando xadrez como as peças " + partida.getJogadorAtual() + ".\n"
             + "Estado atual do tabuleiro:\n"
             + boardState + "\n"
             + "Turno: " + partida.getTurno() + " | Em xeque: " + partida.isCheck() + "\n"
             + "Retorne SOMENTE a jogada no formato: origem destino (ex: e2 e4).\n"
             + "Não escreva mais nada além da jogada. Exemplo de resposta válida: d1 h5";
    }

    // -------------------------------------------------------------------------
    // HTTP com retry
    // -------------------------------------------------------------------------

    private String callWithRetry(String prompt) throws AIException {
        int attempt = 0;
        while (true) {
            attempt++;
            try {
                Thread.sleep(COOLDOWN_MS);
                return callAPI(prompt);
            } catch (AIRateLimitException e) {
                if (attempt >= MAX_RETRIES) throw e;
                System.out.println("[" + NOME + "] " + e.getMessage() + " (tentativa " + attempt + "/" + MAX_RETRIES + ")");
                sleep(e.getRetryAfterMs());
            } catch (AIException e) {
                throw e;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AIException(NOME + ": interrompido durante cooldown.", e);
            }
        }
    }

    private String callAPI(String prompt) throws AIException {
        String body = "{"
                + "\"model\":\"" + MODEL + "\","
                + "\"messages\":[{\"role\":\"user\",\"content\":\"" + escapeJson(prompt) + "\"}],"
                + "\"temperature\":0.2,"
                + "\"max_tokens\":20"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
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
                // tenta ler o header Retry-After (em segundos)
                long retryMs = response.headers().firstValueAsLong("retry-after")
                        .map(s -> s * 1000L)
                        .orElse(5000L);
                throw new AIRateLimitException(NOME, retryMs);
            }
            case 500: case 502: case 503:
                throw new AIException("[" + NOME + "] Erro do servidor (" + status + "): " + responseBody, status);
            default:
                throw new AIException("[" + NOME + "] Resposta inesperada HTTP " + status + ": " + responseBody, status);
        }
    }

    // -------------------------------------------------------------------------
    // JSON helpers (sem dependência externa)
    // -------------------------------------------------------------------------

    private String extractContent(String json) throws AIException {
        // Extrai o campo "content" da resposta OpenAI-compatible
        // {"choices":[{"message":{"content":"e2 e4"}}]}
        int idx = json.indexOf("\"content\":");
        if (idx == -1) throw new AIException(NOME + ": campo 'content' não encontrado na resposta: " + json);
        int start = json.indexOf('"', idx + 10) + 1;
        int end   = json.indexOf('"', start);
        if (start <= 0 || end <= start) throw new AIException(NOME + ": não foi possível extrair 'content' de: " + json);
        return json.substring(start, end);
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
