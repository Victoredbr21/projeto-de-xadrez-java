package chess.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

public class Peao extends PecaXadrez {

    // controla se é o primeiro movimento — libera o avanço duplo
    private boolean primeirMovimento = true;

    // guarda se esse peão acabou de fazer o avanço duplo
    // usado pela PartidaXadrez para verificar en passant do adversário
    private boolean enPassantVulneravel = false;

    public Peao(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    public boolean isPrimeirMovimento() {
        return primeirMovimento;
    }

    public void realizouPrimeirMovimento() {
        this.primeirMovimento = false;
    }

    public boolean isEnPassantVulneravel() {
        return enPassantVulneravel;
    }

    public void setEnPassantVulneravel(boolean enPassantVulneravel) {
        this.enPassantVulneravel = enPassantVulneravel;
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

            // 1 casa pra frente — só se vazia
            p.setValores(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;

                // 2 casas pra frente — só no primeiro movimento e se ambas vazias
                p.setValores(posicao.getLinha() - 2, posicao.getColuna());
                if (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p) && primeirMovimento) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }
            }

            // captura diagonal esquerda — só se tiver adversária
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // captura diagonal direita — só se tiver adversária
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // en passant esquerda
            // o peão adversário está na mesma linha, coluna à esquerda
            // e ficou vulnerável ao en passant (fez avanço duplo no turno anterior)
            p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                PecaXadrez candidato = (PecaXadrez) getTabuleiro().peca(p);
                if (candidato instanceof Peao && ((Peao) candidato).isEnPassantVulneravel()) {
                    mat[posicao.getLinha() - 1][posicao.getColuna() - 1] = true;
                }
            }

            // en passant direita
            p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                PecaXadrez candidato = (PecaXadrez) getTabuleiro().peca(p);
                if (candidato instanceof Peao && ((Peao) candidato).isEnPassantVulneravel()) {
                    mat[posicao.getLinha() - 1][posicao.getColuna() + 1] = true;
                }
            }
        }

        // ===================================================
        // PEÃO PRETO — anda para baixo (linha aumenta)
        // ===================================================
        else {

            // 1 casa pra frente
            p.setValores(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;

                // 2 casas pra frente
                p.setValores(posicao.getLinha() + 2, posicao.getColuna());
                if (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p) && primeirMovimento) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }
            }

            // captura diagonal esquerda
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // captura diagonal direita
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // en passant esquerda
            p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                PecaXadrez candidato = (PecaXadrez) getTabuleiro().peca(p);
                if (candidato instanceof Peao && ((Peao) candidato).isEnPassantVulneravel()) {
                    mat[posicao.getLinha() + 1][posicao.getColuna() - 1] = true;
                }
            }

            // en passant direita
            p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
                PecaXadrez candidato = (PecaXadrez) getTabuleiro().peca(p);
                if (candidato instanceof Peao && ((Peao) candidato).isEnPassantVulneravel()) {
                    mat[posicao.getLinha() + 1][posicao.getColuna() + 1] = true;
                }
            }
        }

        return mat;
    }
}