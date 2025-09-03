package chess;

import boardgame.Tabuleiro;
import chess.pecas.Rei;
import chess.pecas.Torre;
import chess.Cor;

public class PartidaXadrez {
  private Tabuleiro tabuleiro;

  //construtor

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8,8);
        iniciarConfiguracao();
    }

    //metodos

    public PecaXadrez[][] getPecas() {
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinha()][tabuleiro.getColuna()];
        for (int i = 0; i < tabuleiro.getLinha(); i++) {
            for (int j = 0; j < tabuleiro.getColuna(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i,j);

            }
        }
        return mat;
    }

    private void  lugarDaNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.lugarDaPeca(peca, new PosicaoXadrez(coluna, linha).naPosicao());
    }

    private void iniciarConfiguracao() {
        //pecas brancas
        lugarDaNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCO));
        //pecas pretas
        lugarDaNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('e', 7, new Torre(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETO));
    }

    } //marcador