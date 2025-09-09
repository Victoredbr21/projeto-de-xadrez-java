package chess;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;

import java.awt.*;

public abstract class PecaXadrez extends Peca {
    private Cor cor;

    // construtor
    public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    // getter
    public Cor getCor() {
        return cor;
    }
    // metodos
    protected boolean eUmaPecaAdversaria(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }

}//marcador
