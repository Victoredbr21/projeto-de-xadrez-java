package application;

import chess.Cor;
import chess.PartidaXadrez;
import chess.PecaXadrez;
import chess.PosicaoXadrez;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // limpa o terminal
    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static PosicaoXadrez lerPosicaoXadrez(Scanner sc) {
        try {
            String s = sc.nextLine();
            char coluna = s.charAt(0);
            int linha = Integer.parseInt(s.substring(1));
            return new PosicaoXadrez(coluna, linha);
        } catch (Exception e) {
            throw new InputMismatchException("Erros lendo posição de Xadrez, valores válidos são de a1 a h8.");
        }
    }

    public static void imprimirPartida(PartidaXadrez partida, List<PecaXadrez> capturada) {
        imprimirTabuleiro(partida.getPecas());
        imprimirPecasCapturadas(capturada);
        System.out.println();
        System.out.println("Turno : " + partida.getTurno());

        if (partida.isCheckMate()) {
            System.out.println("XEQUE-MATE!");
            System.out.println("Vencedor: " + partida.getJogadorAtual());
        } else {
            System.out.println("Esperando o jogador: " + partida.getJogadorAtual());
            if (partida.isCheck()) {
                System.out.println(ANSI_RED + "*** EM XEQUE! ***" + ANSI_RESET);
            }
        }
    }

    // imprime o tabuleiro sem destacar movimentos
    public static void imprimirTabuleiro(PecaXadrez[][] pecas) {
        for (int i = 0; i < pecas.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pecas.length; j++) {
                imprimirUmaPeca(pecas[i][j], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    // sobrecarga — imprime o tabuleiro destacando os possíveis movimentos em azul
    public static void imprimirTabuleiro(PecaXadrez[][] pecas, boolean[][] possiveisMovimentos) {
        for (int i = 0; i < pecas.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pecas.length; j++) {
                imprimirUmaPeca(pecas[i][j], possiveisMovimentos[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public static void imprimirUmaPeca(PecaXadrez peca, boolean fundo) {
        if (fundo) {
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (peca == null) {
            System.out.print("-" + ANSI_RESET);
        } else {
            if (peca.getCor() == Cor.BRANCO) {
                System.out.print(ANSI_WHITE + peca + ANSI_RESET);
            } else {
                System.out.print(ANSI_YELLOW + peca + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    /**
     * ✨ NOVO: retorna o estado do tabuleiro como String (sem ANSI),
     * para ser enviado no prompt das IAs.
     */
    public static String getBoardAsString(PecaXadrez[][] pecas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pecas.length; i++) {
            sb.append((8 - i)).append(" ");
            for (int j = 0; j < pecas.length; j++) {
                if (pecas[i][j] == null) {
                    sb.append("-");
                } else {
                    sb.append(pecas[i][j].toString());
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        sb.append("  a b c d e f g h");
        return sb.toString();
    }

    private static void imprimirPecasCapturadas(List<PecaXadrez> capturada) {
        List<PecaXadrez> branca = capturada.stream()
                .filter(x -> x.getCor() == Cor.BRANCO)
                .collect(Collectors.toList());
        List<PecaXadrez> preto = capturada.stream()
                .filter(x -> x.getCor() == Cor.PRETO)
                .collect(Collectors.toList());

        System.out.println("Peças capturadas:");
        System.out.print("Brancas: ");
        System.out.print(ANSI_WHITE);
        System.out.println(Arrays.toString(branca.toArray()));
        System.out.println(ANSI_RESET);
        System.out.print("Pretas: ");
        System.out.print(ANSI_YELLOW);
        System.out.println(Arrays.toString(preto.toArray()));
        System.out.println(ANSI_RESET);
    }
}
