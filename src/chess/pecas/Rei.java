package chess.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Rei extends PecaXadrez {

    // flag para saber se o Rei já se moveu — impede o roque se true
    private boolean primeirMovimento = true;

    public Rei(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    public boolean isPrimeirMovimento() {
        return primeirMovimento;
    }

    public void realizouPrimeirMovimento() {
        this.primeirMovimento = false;
    }

    private boolean podeMover(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p == null || p.getCor() != getCor();
    }

    // verifica se a Torre naquela posição ainda não se moveu — condição do roque
    private boolean testeTorreParaRoque(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p instanceof Torre && ((Torre) p).isPrimeirMovimento() && p.getCor() == getCor();
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
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        // abaixo
        p.setValores(posicao.getLinha() + 1, posicao.getColuna());
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        // esquerda
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        // direita
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        // noroeste
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        // nordeste
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        // sudoeste
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        // sudeste
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExistentes(p) && podeMover(p)) mat[p.getLinha()][p.getColuna()] = true;

        // ===================================================
        // ROQUE — só se o Rei nunca se moveu e não está em xeque
        // ===================================================

        // roque pequeno (lado do rei — Torre na coluna +3)
        // verifica: Rei nunca moveu, Torre nunca moveu, 2 casas entre eles livres
        if (primeirMovimento) {
            Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
            if (testeTorreParaRoque(posT1)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
                if (!getTabuleiro().issoEumaPeca(p1) && !getTabuleiro().issoEumaPeca(p2)) {
                    // Rei vai 2 casas pra direita
                    mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
                }
            }

            // roque grande (lado da rainha — Torre na coluna -4)
            // verifica: Rei nunca moveu, Torre nunca moveu, 3 casas entre eles livres
            Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
            if (testeTorreParaRoque(posT2)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
                Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
                if (!getTabuleiro().issoEumaPeca(p1) && !getTabuleiro().issoEumaPeca(p2) && !getTabuleiro().issoEumaPeca(p3)) {
                    // Rei vai 2 casas pra esquerda
                    mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
                }
            }
        }

        return mat;
    }
}