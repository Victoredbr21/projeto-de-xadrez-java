package boardgame;

public class Tabuleiro {
private int linha;
private int coluna;
private Peca[][] pecas;

    //construtor
    public Tabuleiro(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        this.pecas = new Peca[linha][coluna];
    }

     //getters
    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    //setters
    public void setLinha(int linha) {
        this.linha = linha;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    //construtor da peça
    public Peca peca(int linha, int coluna) {
        return pecas[linha][coluna];
    }
    public Peca peca (Posicao posicao) {
        return pecas[posicao.getLinha()][posicao.getColuna()];
    }

}// marcador
