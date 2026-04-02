package chess.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Rainha extends PecaXadrez {

    public Rainha(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString() {
        return (getCor() == Cor.BRANCO) ? "Q" : "q";
    }

    @Override
    public boolean[][] possiveisMovimentos() {
        // matriz de booleanos do tamanho do tabuleiro (8x8)
        // true = casa que a rainha pode se mover
        boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];

        // posição auxiliar que vamos mover pelos loops
        Posicao p = new Posicao(0, 0);


        // --- ACIMA (linha diminui, coluna fixa) ---
        p.setValores(posicao.getLinha() - 1, posicao.getColuna());
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true; // casa livre, pode ir
            p.setValores(p.getLinha() - 1, p.getColuna()); // sobe mais uma casa
        }
        // se parou numa peça adversária, pode capturar
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // --- ABAIXO (linha aumenta, coluna fixa) ---
        p.setValores(posicao.getLinha() + 1, posicao.getColuna());
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha() + 1, p.getColuna());
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // --- ESQUERDA (linha fixa, coluna diminui) ---
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha(), p.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // --- DIREITA (linha fixa, coluna aumenta) ---
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha(), p.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }



        // --- NOROESTE (linha diminui, coluna diminui) ---
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha() - 1, p.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // --- NORDESTE (linha diminui, coluna aumenta) ---
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha() - 1, p.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // --- SUDOESTE (linha aumenta, coluna diminui) ---
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha() + 1, p.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // --- SUDESTE (linha aumenta, coluna aumenta) ---
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setValores(p.getLinha() + 1, p.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        return mat;
    }
}
