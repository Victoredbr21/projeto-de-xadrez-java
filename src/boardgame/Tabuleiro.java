package boardgame;

public class Tabuleiro {
private int linhas;
private int colunas;
private Peca[][] pecas;

    //construtor
    public Tabuleiro(int linha, int coluna) {
        if (linhas < 1 || colunas < 1) {
        throw new BoardException("Erro criando tabuleiro: É necessário que haja pelo menos 1 linha e 1 coluna.");
        }
        this.linhas = linha;
        this.colunas = coluna;
        this.pecas = new Peca[linha][coluna];
    }

     //getters
    public int getLinha() {
        return linhas;
    }

    public int getColuna() {
        return colunas;
    }

    //construtor da peça
    public Peca peca(int linha, int coluna) {
        if (!posicaoExistentes(linha, coluna)) {
        throw new BoardException("Posição não está no tabuleiro.");
        }
        return pecas[linha][coluna];
    }
    public Peca peca (Posicao posicao) {
        if (!posicaoExistentes(posicao)) {
            throw new BoardException("Posição não está no tabuleiro.");
        }
        return pecas[posicao.getLinha()][posicao.getColuna()];
    }

    //metodos

    public void lugarDaPeca(Peca peca, Posicao posicao) {
        if (!issoEumaPeca(posicao)) {
            throw new BoardException("Já existe uma peça na posição" + posicao);
        }
        pecas[posicao.getLinha()][posicao.getColuna()] = peca;  //atribuicao da pecas para o peca da função, tipo o this.peca so que com arrays dimensionais
        peca.posicao = posicao;
    }
    private boolean posicaoExistentes(int linha, int coluna) {
       return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    public boolean posicaoExistentes(Posicao posicao) {
    return posicaoExistentes(posicao.getLinha(), posicao.getColuna());
    }

    public boolean issoEumaPeca (Posicao posicao) {
        if  (!posicaoExistentes(posicao)) {
            throw new BoardException("Essa posição não está no tabuleiro.");
        }
        return peca(posicao) != null;
    }

}// marcador
