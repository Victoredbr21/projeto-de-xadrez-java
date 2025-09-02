package boardgame;

public class Peca {
    protected Posicao posicao;
    private Tabuleiro tabuleiro;

    //construtor
    public Peca(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        posicao = null;
    }
    //getters

    protected Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    }
