package chess;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.pecas.Rei;
import chess.pecas.Torre;
import chess.Cor;

public class PartidaXadrez {
  private Tabuleiro tabuleiro;

  //construtor

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8,8);           // aqui inicio o tabuleiro
        iniciarConfiguracao();
    }

    //metodos

    public PecaXadrez[][] getPecas() { //crio uma array dimensional com as pecas
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinha()][tabuleiro.getColuna()];
        for (int i = 0; i < tabuleiro.getLinha(); i++) {  //for duplo para percorrer as 2 arrays
            for (int j = 0; j < tabuleiro.getColuna(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i,j);

            }
        }
        return mat;
    }

    public PecaXadrez performMovXadrez(PosicaoXadrez fontePosicao, PosicaoXadrez targetPosicao ){   //criando a funcionalidade da peca de xadrez
        Posicao fonte = fontePosicao.naPosicao();     // localizacao da peca
        Posicao target = targetPosicao.naPosicao();   // alvo onde a peca vai
        validarFonteDaPosicao(fonte);
        Peca PecaCapturada = CriarMovimento(fonte, target);
        return (PecaXadrez)  PecaCapturada;
    }

    private Peca CriarMovimento(Posicao fonte, Posicao target){              //aqui eu crio o movimento da peca
        Peca p = tabuleiro.removePeca(fonte);
        Peca pecaCapturada = tabuleiro.removePeca(target);  // a peca que foi capturada eu removo
        tabuleiro.lugarDaPeca(p, target);
        return  pecaCapturada;

    }

    private void validarFonteDaPosicao(Posicao posicao){
        if (!tabuleiro.issoEumaPeca(posicao)){
         throw new ChessExection("Não existe peça na fonte da posição");          //valido a fonte da posicao se der pau provoco um erro aqui
        }
    }

    private void  lugarDaNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.lugarDaPeca(peca, new PosicaoXadrez(coluna, linha).naPosicao());
    }

    private void iniciarConfiguracao() {
        //pecas brancas
        lugarDaNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCO));       //cores que serao usadas
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