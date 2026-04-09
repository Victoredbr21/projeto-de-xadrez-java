package ai;

import application.UI;
import chess.PartidaXadrez;
import chess.PosicaoXadrez;

import java.util.Scanner;

/**
 * Jogador humano que implementa AIPlayer para ser intercambiável
 * com os jogadores IA nos diferentes modos de jogo.
 */
public class HumanPlayer implements AIPlayer {

    private final Scanner sc;
    private final String nome;

    public HumanPlayer(Scanner sc, String nome) {
        this.sc   = sc;
        this.nome = nome;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public PosicaoXadrez[] getMove(String boardState, PartidaXadrez partida) throws AIException {
        try {
            System.out.println("\nPosição de origem: ");
            PosicaoXadrez fonte   = UI.lerPosicaoXadrez(sc);

            boolean[][] movimentos = partida.possiveisMovimentos(fonte);
            UI.limparTela();
            UI.imprimirTabuleiro(partida.getPecas(), movimentos);

            System.out.println("\nDestino: ");
            PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);

            return new PosicaoXadrez[]{fonte, destino};
        } catch (Exception e) {
            throw new AIException("Entrada inválida: " + e.getMessage(), e);
        }
    }
}
