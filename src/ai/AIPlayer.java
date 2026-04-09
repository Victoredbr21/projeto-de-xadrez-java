package ai;

import chess.PosicaoXadrez;
import chess.PartidaXadrez;

/**
 * Contrato que todo jogador IA deve implementar.
 * Recebe o estado completo da partida e retorna a jogada escolhida.
 */
public interface AIPlayer {

    /**
     * @param boardState  representação textual do tabuleiro
     * @param partida     instância da partida para consultar estado
     * @return array com [fonte, destino] em notação xadrez (ex: "e2", "e4")
     */
    PosicaoXadrez[] getMove(String boardState, PartidaXadrez partida) throws AIException;

    /** Nome/label da IA para exibição no terminal */
    String getNome();
}
