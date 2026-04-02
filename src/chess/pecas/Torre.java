package chess.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Torre extends PecaXadrez {

    // flag para saber se a Torre já se moveu — impede o roque se true
    private boolean primeirMovimento = true;

    public Torre(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    public boolean isPrimeirMovimento() {
        return primeirMovimento;
    }

    public void realizouPrimeirMovimento() {
        this.primeirMovimento = false;
    }

    @Override
    public String toString() {
        return (getCor() == Cor.BRANCO) ? "T" : "t";
    }

    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];
        Posicao p = new Posicao(0, 0);

        // acima
        p.setValores(posicao.getLinha() - 1, posicao.getColuna());
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha() - 1, p.getColuna());
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) mat[p.getLinha()][p.getColuna()] = true;

        // abaixo
        p.setValores(posicao.getLinha() + 1, posicao.getColuna());
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha() + 1, p.getColuna());
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) mat[p.getLinha()][p.getColuna()] = true;

        // esquerda
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha(), p.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) mat[p.getLinha()][p.getColuna()] = true;

        // direita
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha(), p.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) mat[p.getLinha()][p.getColuna()] = true;

        return mat;
    }
}