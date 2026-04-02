package chess.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Peao extends PecaXadrez {

    // flag que controla se é o primeiro movimento do peão
    // usada para liberar o movimento duplo inicial
    private boolean primeirMovimento = true;

    public Peao(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    // getter para a PartidaXadrez saber se pode fazer roque/en passant depois
    public boolean isPrimeirMovimento() {
        return primeirMovimento;
    }

    // chamado pela PartidaXadrez após o peão se mover pela primeira vez
    public void realizouPrimeirMovimento() {
        this.primeirMovimento = false;
    }

    @Override
    public String toString() {
        return (getCor() == Cor.BRANCO) ? "P" : "p";
    }

    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];

        Posicao p = new Posicao(0, 0);

        // ===================================================
        // PEÃO BRANCO — anda para cima (linha diminui)
        // ===================================================
        if (getCor() == Cor.BRANCO) {

            // --- 1 casa pra frente ---
            // peão SÓ anda pra frente se a casa estiver VAZIA
            // (diferente das outras peças que capturam pra frente)
            p.setValores(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;

                // --- 2 casas pra frente (só no primeiro movimento) ---
                // só verifica o duplo se a primeira casa já estiver livre
                // (não pode pular por cima de uma peça)
                p.setValores(posicao.getLinha() - 2, posicao.getColuna());
                if (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p) && primeirMovimento) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }
            }

            // --- captura diagonal esquerda ---
            // peão SÓ captura na diagonal — nunca pra frente
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // --- captura diagonal direita ---
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
        }

        // ===================================================
        // PEÃO PRETO — anda para baixo (linha aumenta)
        // espelho exato do branco, só inverte o sinal da linha
        // ===================================================
        else {

            // --- 1 casa pra frente ---
            p.setValores(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;

                // --- 2 casas pra frente (só no primeiro movimento) ---
                p.setValores(posicao.getLinha() + 2, posicao.getColuna());
                if (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p) && primeirMovimento) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }
            }

            // --- captura diagonal esquerda ---
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // --- captura diagonal direita ---
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
        }

        return mat;
    }
}
