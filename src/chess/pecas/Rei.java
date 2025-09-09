package chess.pecas;

import boardgame.Posicao;
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
    //metodos

    private boolean podeMover(Posicao posicao){
     PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
     return p == null || p.getCor() != cor;
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

       Posicao p = new Posicao(0, 0);

       //acima

        p.setValores(posicao.getLinha() - 1, posicao.getColuna());
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

          //abaixo

           p.setValores(posicao.getLinha() + 1, posicao.getColuna());
           if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
           }
           //esquerda

            p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
              }

            //direita
            p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;

            //noroeste
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            //nordeste
                p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }
            //sudoeste
                p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }
                //sudeste
                p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }
        }
        return mat;
    }
}