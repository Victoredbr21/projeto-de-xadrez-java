package chess;

import boardgame.Peca;
import boardgame.Tabuleiro;

import java.awt.*;

public class PecaXadrez extends Peca {
    private Color color;

    //construtor
    public PecaXadrez(Tabuleiro tabuleiro, Color color) {
        super (Tabuleiro);
        this.color = color;
    }
    //getter
    public Color getColor() {
        return color;
    }

}//marcador
