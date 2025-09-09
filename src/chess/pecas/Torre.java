package chess.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaXadrez;

import static java.awt.Color.getColor;

public class Torre extends PecaXadrez {
    // Constantes estáticas para acesso direto
    public static final String BRANCO = "♜";
    public static final String PRETO = "♖";
    private final Cor Cor;

    // Construtor
    public Torre(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
        this.Cor = cor;
    }

    @Override
    public String toString() {
        if (getCor() == Cor.BRANCO) {
            return "♜"; // branca visualmente “preta”
        } else {
            return "♖"; // preta visualmente “branca”
        }
    }
    @Override
    public boolean[][] possiveisMovimentos() {
        boolean[][] mat = new boolean[getTabuleiro().getLinha()][getTabuleiro().getColuna()];
        Posicao p = new Posicao(0,0);

        //acima
        p.setValores(posicao.getLinha() - 1,  posicao.getColuna());
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)){ // enquanto a posicao existir e nao tiver uma peça la
        mat[p.getLinha()][p.getColuna()] = true;
        p.setLinha(p.getLinha() - 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }
        //esquerda
        p.setValores(posicao.getLinha(),  posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)){ // enquanto a posicao existir e nao tiver uma peça la
            mat[p.getLinha()][p.getColuna()] = true;
            p.setColuna(p.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }
        //direita

        //esquerda
        p.setValores(posicao.getLinha(),  posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)){ // enquanto a posicao existir e nao tiver uma peça la
            mat[p.getLinha()][p.getColuna()] = true;
            p.setColuna(p.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }
        // baixo
        p.setValores(posicao.getLinha() + 1,  posicao.getColuna());
        while (getTabuleiro().posicaoExistentes(p) && !getTabuleiro().issoEumaPeca(p)){ // enquanto a posicao existir e nao tiver uma peça la
            mat[p.getLinha()][p.getColuna()] = true;
            p.setLinha(p.getLinha() + 1);
        }
        if (getTabuleiro().posicaoExistentes(p) && eUmaPecaAdversaria(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        return mat;
    }

}