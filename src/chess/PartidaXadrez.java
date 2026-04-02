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

    // guarda o peão vulnerável ao en passant no turno atual
    private Peao enPassantVulneravel;

    // guarda a peça promovida para a UI poder pedir o input
    private PecaXadrez promovido;

    private List<Peca> pecasNoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        jogadorAtual = Cor.BRANCO;
        iniciarConfiguracao();
    }

    // ===================================================
    // GETTERS
    // ===================================================

    public int getTurno() { return turno; }
    public Cor getJogadorAtual() { return jogadorAtual; }
    public boolean isCheck() { return check; }
    public boolean isCheckMate() { return checkMate; }
    public PecaXadrez getPromovido() { return promovido; }
    public Peao getEnPassantVulneravel() { return enPassantVulneravel; }

    public PecaXadrez[][] getPecas() {
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinha()][tabuleiro.getColuna()];
        for (int i = 0; i < tabuleiro.getLinha(); i++)
            for (int j = 0; j < tabuleiro.getColuna(); j++)
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
        return mat;
    }

    public boolean[][] possiveisMovimentos(PosicaoXadrez fontePosicao) {
        Posicao posicao = fontePosicao.naPosicao();
        validarFonteDaPosicao(posicao);
        return tabuleiro.peca(posicao).possiveisMovimentos();
    }

    // ===================================================
    // EXECUÇÃO DO MOVIMENTO
    // ===================================================

    public PecaXadrez performMovXadrez(PosicaoXadrez fontePosicao, PosicaoXadrez targetPosicao) {
        Posicao fonte = fontePosicao.naPosicao();
        Posicao target = targetPosicao.naPosicao();

        validarFonteDaPosicao(fonte);
        validarTargetPosicao(fonte, target);

        Peca pecaCapturada = criarMovimento(fonte, target);

        // protege contra auto-xeque
        if (testeXeque(jogadorAtual)) {
            desfazerMovimento(fonte, target, pecaCapturada);
            throw new ChessExection("Você não pode se colocar em xeque!");
        }

        PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.peca(target);

        // --- promoção do peão ---
        // se o peão chegou na última linha, fica guardado em promovido
        // a UI vai chamar substituirPecaPromovida() com a escolha do jogador
        promovido = null;
        if (pecaMovida instanceof Peao) {
            if ((pecaMovida.getCor() == Cor.BRANCO && target.getLinha() == 0) ||
                    (pecaMovida.getCor() == Cor.PRETO && target.getLinha() == 7)) {
                promovido = pecaMovida;
                // por padrão promove pra Rainha — a UI pode substituir depois
                substituirPecaPromovida("Q");
            }
        }

        // --- en passant: marca o peão vulnerável ---
        enPassantVulneravel = null;
        if (pecaMovida instanceof Peao && Math.abs(target.getLinha() - fonte.getLinha()) == 2) {
            // peão avançou 2 casas — fica vulnerável ao en passant no próximo turno
            enPassantVulneravel = (Peao) pecaMovida;
            enPassantVulneravel.setEnPassantVulneravel(true);
        }

        // --- flags de primeiro movimento ---
        if (pecaMovida instanceof Peao) ((Peao) pecaMovida).realizouPrimeirMovimento();
        if (pecaMovida instanceof Torre) ((Torre) pecaMovida).realizouPrimeirMovimento();
        if (pecaMovida instanceof Rei) ((Rei) pecaMovida).realizouPrimeirMovimento();

        check = testeXeque(oponente(jogadorAtual));
        checkMate = check && testeXequeMate(oponente(jogadorAtual));

        proximoTurno();
        return (PecaXadrez) pecaCapturada;
    }

    // substitui o peão promovido pela peça escolhida pelo jogador
    // tipo: "Q" = Rainha, "T" = Torre, "B" = Bispo, "C" = Cavalo
    public PecaXadrez substituirPecaPromovida(String tipo) {
        if (promovido == null) {
            throw new IllegalStateException("Não há peça para promover");
        }

        Posicao pos = localizarPeca(promovido);
        Peca p = tabuleiro.removePeca(pos);
        pecasNoTabuleiro.remove(p);

        PecaXadrez novaPeca;
        switch (tipo.toUpperCase()) {
            case "T": novaPeca = new Torre(tabuleiro, promovido.getCor()); break;
            case "B": novaPeca = new Bispo(tabuleiro, promovido.getCor()); break;
            case "C": novaPeca = new Cavalo(tabuleiro, promovido.getCor()); break;
            default:  novaPeca = new Rainha(tabuleiro, promovido.getCor()); break;
        }

        tabuleiro.lugarDaPeca(novaPeca, pos);
        pecasNoTabuleiro.add(novaPeca);
        return novaPeca;
    }

    // ===================================================
    // LÓGICA DE XEQUE
    // ===================================================

    private boolean testeXeque(Cor cor) {
        Posicao posicaoRei = localizarRei(cor);
        List<Peca> adversarias = pecasNoTabuleiro.stream()
                .filter(x -> ((PecaXadrez) x).getCor() == oponente(cor))
                .collect(Collectors.toList());

        for (Peca p : adversarias) {
            boolean[][] mat = p.possiveisMovimentos();
            if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) return true;
        }
        return false;
    }

    private boolean testeXequeMate(Cor cor) {
        if (!testeXeque(cor)) return false;

        List<Peca> lista = pecasNoTabuleiro.stream()
                .filter(x -> ((PecaXadrez) x).getCor() == cor)
                .collect(Collectors.toList());

        for (Peca p : lista) {
            boolean[][] mat = p.possiveisMovimentos();
            for (int i = 0; i < tabuleiro.getLinha(); i++) {
                for (int j = 0; j < tabuleiro.getColuna(); j++) {
                    if (mat[i][j]) {
                        Posicao fonte = localizarPeca(p);
                        Posicao target = new Posicao(i, j);
                        Peca capturada = criarMovimento(fonte, target);
                        boolean aindaEmXeque = testeXeque(cor);
                        desfazerMovimento(fonte, target, capturada);
                        if (!aindaEmXeque) return false;
                    }
                }
            }
        }
        return true;
    }

    // ===================================================
    // MOVIMENTOS INTERNOS
    // ===================================================

    private Peca criarMovimento(Posicao fonte, Posicao target) {
        PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(fonte);
        Peca pecaCapturada = tabuleiro.removePeca(target);
        tabuleiro.lugarDaPeca(p, target);

        if (pecaCapturada != null) {
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }

        // --- roque pequeno ---
        // Rei moveu 2 casas pra direita — move a Torre junto
        if (p instanceof Rei && target.getColuna() == fonte.getColuna() + 2) {
            Posicao fonteT = new Posicao(fonte.getLinha(), fonte.getColuna() + 3);
            Posicao targetT = new Posicao(fonte.getLinha(), fonte.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(fonteT);
            tabuleiro.lugarDaPeca(torre, targetT);
        }

        // --- roque grande ---
        // Rei moveu 2 casas pra esquerda — move a Torre junto
        if (p instanceof Rei && target.getColuna() == fonte.getColuna() - 2) {
            Posicao fonteT = new Posicao(fonte.getLinha(), fonte.getColuna() - 4);
            Posicao targetT = new Posicao(fonte.getLinha(), fonte.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(fonteT);
            tabuleiro.lugarDaPeca(torre, targetT);
        }

        // --- en passant ---
        // peão capturou na diagonal mas a casa estava vazia — remove o peão capturado
        if (p instanceof Peao && fonte.getColuna() != target.getColuna() && pecaCapturada == null) {
            Posicao posicaoPeao = (p.getCor() == Cor.BRANCO)
                    ? new Posicao(target.getLinha() + 1, target.getColuna())
                    : new Posicao(target.getLinha() - 1, target.getColuna());
            pecaCapturada = tabuleiro.removePeca(posicaoPeao);
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }

        return pecaCapturada;
    }

    private void desfazerMovimento(Posicao fonte, Posicao target, Peca pecaCapturada) {
        PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(target);
        tabuleiro.lugarDaPeca(p, fonte);

        if (pecaCapturada != null) {
            tabuleiro.lugarDaPeca(pecaCapturada, target);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }

        // desfaz roque pequeno
        if (p instanceof Rei && target.getColuna() == fonte.getColuna() + 2) {
            Posicao fonteT = new Posicao(fonte.getLinha(), fonte.getColuna() + 3);
            Posicao targetT = new Posicao(fonte.getLinha(), fonte.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(targetT);
            tabuleiro.lugarDaPeca(torre, fonteT);
        }

        // desfaz roque grande
        if (p instanceof Rei && target.getColuna() == fonte.getColuna() - 2) {
            Posicao fonteT = new Posicao(fonte.getLinha(), fonte.getColuna() - 4);
            Posicao targetT = new Posicao(fonte.getLinha(), fonte.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(targetT);
            tabuleiro.lugarDaPeca(torre, fonteT);
        }

        // desfaz en passant
        if (p instanceof Peao && fonte.getColuna() != target.getColuna() && pecaCapturada instanceof Peao) {
            Posicao posicaoPeao = (p.getCor() == Cor.BRANCO)
                    ? new Posicao(target.getLinha() + 1, target.getColuna())
                    : new Posicao(target.getLinha() - 1, target.getColuna());
            tabuleiro.lugarDaPeca(tabuleiro.removePeca(target), posicaoPeao);
        }
    }

    // ===================================================
    // HELPERS
    // ===================================================

    private Cor oponente(Cor cor) {
        return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private Posicao localizarRei(Cor cor) {
        for (int i = 0; i < tabuleiro.getLinha(); i++)
            for (int j = 0; j < tabuleiro.getColuna(); j++) {
                Peca p = tabuleiro.peca(i, j);
                if (p instanceof Rei && ((PecaXadrez) p).getCor() == cor)
                    return new Posicao(i, j);
            }
        throw new IllegalStateException("Não existe o Rei da cor " + cor + " no tabuleiro");
    }

    private Posicao localizarPeca(Peca peca) {
        for (int i = 0; i < tabuleiro.getLinha(); i++)
            for (int j = 0; j < tabuleiro.getColuna(); j++)
                if (tabuleiro.peca(i, j) == peca) return new Posicao(i, j);
        throw new IllegalStateException("Peça não encontrada no tabuleiro");
    }

    // ===================================================
    // VALIDAÇÕES
    // ===================================================

    private void validarFonteDaPosicao(Posicao posicao) {
        if (!tabuleiro.issoEumaPeca(posicao))
            throw new ChessExection("Não existe peça na fonte da posição");
        if (jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor())
            throw new ChessExection("A peça escolhida não é sua");
        if (!tabuleiro.peca(posicao).ePossivelMover())
            throw new ChessExection("Não existe movimentos possíveis para a peça escolhida");
    }

    private void validarTargetPosicao(Posicao fonte, Posicao target) {
        if (!tabuleiro.peca(fonte).possiveisMovimentos(target))
            throw new ChessExection("A peça escolhida não pode mover para o destino escolhido");
    }

    private void proximoTurno() {
        turno++;
        jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
        // reseta en passant do turno anterior
        // só vale por 1 turno — se não capturou, perdeu a chance
        pecasNoTabuleiro.stream()
                .filter(x -> x instanceof Peao && ((Peao) x).isEnPassantVulneravel())
                .forEach(x -> ((Peao) x).setEnPassantVulneravel(false));
    }

    private void lugarDaNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.lugarDaPeca(peca, new PosicaoXadrez(coluna, linha).naPosicao());
        pecasNoTabuleiro.add(peca);
    }

    // ===================================================
    // SETUP
    // ===================================================

    private void iniciarConfiguracao() {
        lugarDaNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
        lugarDaNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
        for (char c = 'a'; c <= 'h'; c++) lugarDaNovaPeca(c, 2, new Peao(tabuleiro, Cor.BRANCO));

        lugarDaNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
        lugarDaNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO));
        for (char c = 'a'; c <= 'h'; c++) lugarDaNovaPeca(c, 7, new Peao(tabuleiro, Cor.PRETO));
    }
}