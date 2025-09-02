package chess;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.pecas.Rei;
import chess.pecas.Torre;
import chess.Cor;

public class PartidaXadrez {
  private Tabuleiro tabuleiro;

  //construtor

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8,8);
        iniciarTabuleiro();
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

    private void iniciarTabuleiro() {
        tabuleiro.lugarDaPeca(new Torre(tabuleiro, Cor.BRANCO), new Posicao(2,1));
        tabuleiro.lugarDaPeca(new Rei(tabuleiro, Cor.PRETO), new Posicao(0,4));
        tabuleiro.lugarDaPeca(new Rei(tabuleiro, Cor.BRANCO), new Posicao(0,4));
    }

} //marcador
