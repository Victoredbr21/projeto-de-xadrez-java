package chess.pecas;

import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

import static java.awt.Color.getColor;

public class Torre extends PecaXadrez {
    // Constantes estáticas para acesso direto
    public static final String BRANCO = "♜";
    public static final String PRETO = "♖";
    private final Cor Cor;

    // Construtor
    public Torre(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
        this.Cor = cor;
    }

    @Override
    public String toString() {
        if (getCor() == Cor.BRANCO) {
            return "♜"; // branca visualmente “preta”
        } else {
            return "♖"; // preta visualmente “branca”
        }
    }
    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];
        return mat;
    }

}