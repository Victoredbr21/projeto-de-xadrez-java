package chess.pecas;

import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Rei extends PecaXadrez {
    // Constantes estáticas para acesso direto
    public static final String BRANCO = "♔";
    public static final String PRETO = "♚";

    // Construtor
    public Rei(Tabuleiro tabuleiro, Cor cor) {

        super(tabuleiro, Cor);
    }

    @Override
    public String toString() {
        if(Cor() == Cor.PRETO) {
            return PRETO;
        } else {
            return BRANCO;
        }
    }
}