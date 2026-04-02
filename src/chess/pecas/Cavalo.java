
package chess.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Cavalo extends PecaXadrez {

    public Cavalo(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString() {
        return (getCor() == Cor.BRANCO) ? "C" : "c";
    }

    private boolean podeMover(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p == null || p.getCor() != getCor();
    }

    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];

        Posicao p = new Posicao(0, 0);

        // 8 posições em L — 2 pra um lado, 1 pro outro
        p.setValores(posicao.getLinha() - 2, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() - 2, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 2);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 2);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 2);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 2);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() + 2, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        p.setValores(posicao.getLinha() + 2, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        return mat;
    }
}