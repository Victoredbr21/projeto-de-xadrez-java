package chess;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.pecas.Bispo;
import chess.pecas.Cavalo;
import chess.pecas.Peao;
import chess.pecas.Rainha;
import chess.pecas.Rei;
import chess.pecas.Torre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PartidaXadrez {

    private int turno;
    private Tabuleiro tabuleiro;
    private Cor jogadorAtual;
    private boolean check;
    private boolean checkMate;

    private List<Peca> pecasNoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();

    // ===================================================
    // CONSTRUTOR
    // ===================================================

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        jogadorAtual = Cor.BRANCO;
        iniciarConfiguracao();
    }

    // ===================================================
    // GETTERS
    // ===================================================

    public int getTurno() {
        return turno;
    }

    public Cor getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    // ===================================================
    // MÉTODOS PÚBLICOS
    // ===================================================

    // retorna a matriz de peças para a UI renderizar
    public PecaXadrez[][] getPecas() {
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinha()][tabuleiro.getColuna()];
        for (int i = 0; i < tabuleiro.getLinha(); i++) {
            for (int j = 0; j < tabuleiro.getColuna(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
            }
        }
        return mat;
    }

    // retorna os possíveis movimentos de uma peça para destacar na UI
    public boolean[][] possiveisMovimentos(PosicaoXadrez fontePosicao) {
        Posicao posicao = fontePosicao.naPosicao();
        validarFonteDaPosicao(posicao);
        return tabuleiro.peca(posicao).possiveisMovimentos();
    }

    // executa o movimento escolhido pelo jogador
    public PecaXadrez performMovXadrez(PosicaoXadrez fontePosicao, PosicaoXadrez targetPosicao) {
        Posicao fonte = fontePosicao.naPosicao();
        Posicao target = targetPosicao.naPosicao();

        validarFonteDaPosicao(fonte);
        validarTargetPosicao(fonte, target);

        Peca pecaCapturada = criarMovimento(fonte, target);

        // após mover, verifica se o próprio jogador deixou seu Rei em xeque
        // se sim, desfaz o movimento e lança exceção — movimento ilegal
        if (testeXeque(jogadorAtual)) {
            desfazerMovimento(fonte, target, pecaCapturada);
            throw new ChessExection("Você não pode se colocar em xeque!");
        }

        // atualiza a flag de primeiro movimento do Peão se necessário
        atualizarPrimeirMovimentoPeao(target);

        // atualiza a flag de xeque para o adversário
        check = testeXeque(oponente(jogadorAtual));

        // verifica se o adversário entrou em xeque-mate
        checkMate = check && testeXequeMate(oponente(jogadorAtual));

        proximoTurno();
        return (PecaXadrez) pecaCapturada;
    }

    // ===================================================
    // LÓGICA DE XEQUE
    // ===================================================

    // verifica se o Rei da cor informada está em xeque
    // percorre todas as peças adversárias e vê se alguma alcança o Rei
    private boolean testeXeque(Cor cor) {
        Posicao posicaoRei = localizarRei(cor);

        List<Peca> pecasAdversarias = pecasNoTabuleiro.stream()
                .filter(x -> ((PecaXadrez) x).getCor() == oponente(cor))
                .collect(Collectors.toList());

        for (Peca p : pecasAdversarias) {
            boolean[][] mat = p.possiveisMovimentos();
            if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
                return true; // Rei está em xeque
            }
        }
        return false;
    }

    // verifica se a cor informada está em xeque-mate
    // testa todos os movimentos possíveis — se nenhum tirar do xeque, é xeque-mate
    private boolean testeXequeMate(Cor cor) {
        if (!testeXeque(cor)) {
            return false;
        }

        List<Peca> lista = pecasNoTabuleiro.stream()
                .filter(x -> ((PecaXadrez) x).getCor() == cor)
                .collect(Collectors.toList());

        for (Peca p : lista) {
            boolean[][] mat = p.possiveisMovimentos();
            for (int i = 0; i < tabuleiro.getLinha(); i++) {
                for (int j = 0; j < tabuleiro.getColuna(); j++) {
                    if (mat[i][j]) {
                        // descobre a posição atual da peça varrendo o tabuleiro
                        Posicao fonte = localizarPeca(p);
                        Posicao target = new Posicao(i, j);

                        Peca pecaCapturada = criarMovimento(fonte, target);
                        boolean aindaEmXeque = testeXeque(cor);
                        desfazerMovimento(fonte, target, pecaCapturada);

                        if (!aindaEmXeque) {
                            return false; // achou um movimento que sai do xeque
                        }
                    }
                }
            }
        }
        return true; // nenhum movimento tirou do xeque — xeque-mate
    }

    // ===================================================
    // MOVIMENTOS INTERNOS
    // ===================================================

    private Peca criarMovimento(Posicao fonte, Posicao target) {
        Peca p = tabuleiro.removePeca(fonte);
        Peca pecaCapturada = tabuleiro.removePeca(target);
        tabuleiro.lugarDaPeca(p, target);

        if (pecaCapturada != null) {
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }
        return pecaCapturada;
    }

    // desfaz um movimento — usado para testar xeque sem efeito colateral
    private void desfazerMovimento(Posicao fonte, Posicao target, Peca pecaCapturada) {
        Peca p = tabuleiro.removePeca(target);
        tabuleiro.lugarDaPeca(p, fonte);

        if (pecaCapturada != null) {
            tabuleiro.lugarDaPeca(pecaCapturada, target);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }
    }

    // atualiza flag de primeiro movimento do Peão após ele se mover
    private void atualizarPrimeirMovimentoPeao(Posicao target) {
        Peca p = tabuleiro.peca(target);
        if (p instanceof Peao) {
            ((Peao) p).realizouPrimeirMovimento();
        }
    }

    // ===================================================
    // HELPERS
    // ===================================================

    // retorna a cor adversária
    private Cor oponente(Cor cor) {
        return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    // localiza a posição do Rei de uma cor varrendo o tabuleiro
    private Posicao localizarRei(Cor cor) {
        for (int i = 0; i < tabuleiro.getLinha(); i++) {
            for (int j = 0; j < tabuleiro.getColuna(); j++) {
                Peca p = tabuleiro.peca(i, j);
                if (p instanceof Rei && ((PecaXadrez) p).getCor() == cor) {
                    return new Posicao(i, j);
                }
            }
        }
        throw new IllegalStateException("Não existe o Rei da cor " + cor + " no tabuleiro");
    }

    // localiza a posição de qualquer peça varrendo o tabuleiro
    private Posicao localizarPeca(Peca peca) {
        for (int i = 0; i < tabuleiro.getLinha(); i++) {
            for (int j = 0; j < tabuleiro.getColuna(); j++) {
                if (tabuleiro.peca(i, j) == peca) {
                    return new Posicao(i, j);
                }
            }
        }
        throw new IllegalStateException("Peça não encontrada no tabuleiro");
    }

    // ===================================================
    // VALIDAÇÕES
    // ===================================================

    private void validarFonteDaPosicao(Posicao posicao) {
        if (!tabuleiro.issoEumaPeca(posicao)) {
            throw new ChessExection("Não existe peça na fonte da posição");
        }
        if (jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
            throw new ChessExection("A peça escolhida não é sua");
        }
        if (!tabuleiro.peca(posicao).ePossivelMover()) {
            throw new ChessExection("Não existe movimentos possíveis para a peça escolhida");
        }
    }

    private void validarTargetPosicao(Posicao fonte, Posicao target) {
        if (!tabuleiro.peca(fonte).possiveisMovimentos(target)) {
            throw new ChessExection("A peça escolhida não pode mover para o destino escolhido");
        }
    }

    private void proximoTurno() {
        turno++;
        jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private void lugarDaNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.lugarDaPeca(peca, new PosicaoXadrez(coluna, linha).naPosicao());
        pecasNoTabuleiro.add(peca);
    }

    // ===================================================
    // SETUP — posições reais do xadrez
    // ===================================================

    private void iniciarConfiguracao() {
        // peças brancas
        lugarDaNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
        for (char c = 'a'; c <= 'h'; c++) {
            lugarDaNovaPeca(c, 2, new Peao(tabuleiro, Cor.BRANCO));
        }

        // peças pretas
        lugarDaNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO));
        for (char c = 'a'; c <= 'h'; c++) {
            lugarDaNovaPeca(c, 7, new Peao(tabuleiro, Cor.PRETO));
        }
    }
}