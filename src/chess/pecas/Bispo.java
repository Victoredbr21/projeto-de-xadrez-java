package chess.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Bispo extends PecaXadrez {

    public Bispo(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString() {
        return (getCor() == Cor.BRANCO) ? "B" : "b";
    }

    @Override
    public boolean[][] possiveisMovimentos() {
        // matriz de booleanos do tamanho do tabuleiro (8x8)
        // true = casa que o bispo pode se mover
        boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];

        // posição auxiliar que vamos mover pelos loops
        Posicao p = new Posicao(0, 0);

        // --- NOROESTE (linha diminui, coluna diminui) ---
        // começa uma casa acima e uma à esquerda da posição atual
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
        // enquanto a posição existir no tabuleiro E não tiver nenhuma peça bloqueando
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true; // casa livre, pode ir
            p.setValores(p.getLinha() - 1, p.getColuna() - 1); // avança mais uma casa na diagonal
        }
        // saiu do while: ou chegou na borda ou encontrou uma peça
        // se encontrou uma peça adversária, pode capturar — marca como true também
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
        // no tabuleiro a linha aumenta indo para baixo (índice 0 = linha 8)
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
