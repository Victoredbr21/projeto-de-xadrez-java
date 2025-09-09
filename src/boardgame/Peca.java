package boardgame;

public abstract class Peca {
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

    public abstract boolean [][] possiveisMovimentos();

    //metodos

    public boolean possiveisMovimentos(Posicao posicao) {
        return possiveisMovimentos()[posicao.getLinha()][posicao.getColuna()];
    }
    public boolean ePossivelMover(){
        boolean[][] mat = possiveisMovimentos();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    } //fecha a classe
