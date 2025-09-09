package chess;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.pecas.Rei;
import chess.pecas.Torre;
import chess.Cor;

import java.util.ArrayList;
import java.util.List;

public class PartidaXadrez {
  private int turno;
  private Tabuleiro tabuleiro;
  private Cor jogadorAtual;

  private List<Peca> pecasNoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();
  //construtor

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8,8); // aqui inicio o tabuleiro
        turno = 1;
        jogadorAtual = Cor.BRANCO;
        iniciarConfiguracao();
    }
    //getters

    public int getTurno() {
        return turno;
    }
    public Cor getJogadorAtual() {
        return jogadorAtual;
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

        public boolean[][] possiveisMovimentos(PosicaoXadrez fontePosicao){
        Posicao posicao = fontePosicao.naPosicao();
        validarFonteDaPosicao(posicao);
        return tabuleiro.peca(posicao).possiveisMovimentos();
        }

    public PecaXadrez performMovXadrez(PosicaoXadrez fontePosicao, PosicaoXadrez targetPosicao ){   //criando a funcionalidade da peca de xadrez
        Posicao fonte = fontePosicao.naPosicao();     // localizacao da peca
        Posicao target = targetPosicao.naPosicao();   // alvo onde a peca vai
        validarFonteDaPosicao(fonte);
        validarTargetPosicao(fonte, target);
        Peca PecaCapturada = CriarMovimento(fonte, target);
        proximoTurno();
        return (PecaXadrez)  PecaCapturada;
    }

    private Peca CriarMovimento(Posicao fonte, Posicao target){              //aqui eu crio o movimento da peca
        Peca p = tabuleiro.removePeca(fonte);
        Peca pecaCapturada = tabuleiro.removePeca(target);  // a peca que foi capturada eu removo
        tabuleiro.lugarDaPeca(p, target);

        if (pecaCapturada != null) {
           pecasNoTabuleiro.remove(pecaCapturada);
           pecasCapturadas.add(pecaCapturada);
        }

        return  pecaCapturada;

    }

    private void validarFonteDaPosicao(Posicao posicao){
        if (!tabuleiro.issoEumaPeca(posicao)){
         throw new ChessExection("Não existe peça na fonte da posição");          //valido a fonte da posicao se der pau provoco um erro aqui
        }
        if (jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()){
           throw new ChessExection("a peça escolhida não é sua");
        }

        if(!tabuleiro.peca(posicao).ePossivelMover()) {
            throw new ChessExection("Não existe movimentos possíveis para a peça escolhido");
        }
    }

    private void validarTargetPosicao(Posicao fonte, Posicao target){
        if (!tabuleiro.peca(fonte).possiveisMovimentos(target)) {
           throw new ChessExection("A peça escolhida não pode mover para o destino escolhido");
        }
    }
    private void proximoTurno() {
        turno++;
        jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private void  lugarDaNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.lugarDaPeca(peca, new PosicaoXadrez(coluna, linha).naPosicao());
        pecasNoTabuleiro.add(peca);
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