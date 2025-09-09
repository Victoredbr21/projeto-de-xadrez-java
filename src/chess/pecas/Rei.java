package chess.pecas;

import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Rei extends PecaXadrez {
    // Constantes para acesso direto
    public static final String BRANCO = "♚";
    public static final String PRETO = "♔";
    private final Cor cor;

    // Construtor
    public Rei(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
        this.cor = cor;
    }

    @Override
    public String toString() {
        if (getCor() == Cor.BRANCO) {
            return "♚"; // branca visualmente “preta”
        } else {
            return "♔"; // preta visualmente “branca”
        }
    }
    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];
        return mat;
    }
}