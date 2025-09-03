package chess;

import boardgame.Posicao;

public class PosicaoXadrez {
    private char coluna;
    private int linha;

    public PosicaoXadrez(char coluna, int linha) {
        if (coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
            throw new ChessExection("Erro instanciando posição de xadrez, valores válidos são de a1 até h8");
        }
        this.coluna = coluna;
        this.linha = linha;
    }

    //getters

    public char getColuna() {
        return coluna;
    }

    public int getLinha() {
        return linha;
    }

    //metodos
     protected Posicao naPosicao() {                 // aqui é importante entender 'a' - 'a' = 0, 'b' - 'a' = 1, assim em diante, como funciona na matriz vetorial
    return new Posicao(8 - linha, coluna - 'a');
     }

     protected PosicaoXadrez paraPosicao(Posicao posicao){
        return PosicaoXadrez((char)('a' - posicao.getColuna()), 8 - posicao.getLinha());
     }

     @Override
     public String toString() {
       return "" + coluna + linha;
     }

} //marcador
