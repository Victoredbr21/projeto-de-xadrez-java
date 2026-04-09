package ai;

import chess.PosicaoXadrez;

/**
 * Responsável por extrair e validar a jogada retornada pela IA.
 * A IA deve retornar algo no formato "e2 e4" (origem destino).
 * Este parser tenta extrair esse padrão mesmo que venha com texto extra.
 */
public class MoveParser {

    // Regex: duas posições no formato [a-h][1-8] separadas por espaço
    private static final java.util.regex.Pattern MOVE_PATTERN =
            java.util.regex.Pattern.compile("([a-h][1-8])\\s+([a-h][1-8])");

    /**
     * Extrai a jogada de uma string que pode conter texto adicional da IA.
     *
     * @param rawResponse resposta bruta da API
     * @return array [fonte, destino] como PosicaoXadrez
     * @throws AIException se não encontrar um par de posições válido
     */
    public static PosicaoXadrez[] parse(String rawResponse) throws AIException {
        if (rawResponse == null || rawResponse.isBlank()) {
            throw new AIException("Resposta da IA veio vazia.");
        }

        String clean = rawResponse.trim().toLowerCase();
        java.util.regex.Matcher matcher = MOVE_PATTERN.matcher(clean);

        if (!matcher.find()) {
            throw new AIException("Não foi possível extrair uma jogada válida da resposta: \"" + rawResponse + "\"");
        }

        String fonteStr  = matcher.group(1); // ex: "e2"
        String destinoStr = matcher.group(2); // ex: "e4"

        PosicaoXadrez fonte  = parsePosicao(fonteStr);
        PosicaoXadrez destino = parsePosicao(destinoStr);

        return new PosicaoXadrez[]{fonte, destino};
    }

    private static PosicaoXadrez parsePosicao(String pos) throws AIException {
        try {
            char coluna = pos.charAt(0);
            int linha = Integer.parseInt(pos.substring(1));
            return new PosicaoXadrez(coluna, linha);
        } catch (Exception e) {
            throw new AIException("Posição inválida na resposta da IA: \"" + pos + "\"");
        }
    }
}
