package chess.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Rei extends PecaXadrez {

    public Rei(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    private boolean podeMover(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p == null || p.getCor() != getCor();
    }

    @Override
    public String toString() {
        return (getCor() == Cor.BRANCO) ? "R" : "r";
    }

    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];

        Posicao p = new Posicao(0, 0);

        // acima
        p.setValores(posicao.getLinha() - 1, posicao.getColuna());
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // abaixo
        p.setValores(posicao.getLinha() + 1, posicao.getColuna());
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // esquerda
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // direita
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // noroeste
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // nordeste
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // sudoeste
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // sudeste
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        return mat;
    }
}