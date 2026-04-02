package application;

import chess.ChessExection;
import chess.PartidaXadrez;
import chess.PecaXadrez;
import chess.PosicaoXadrez;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Programa {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PartidaXadrez partida = new PartidaXadrez();
        List<PecaXadrez> capturada = new ArrayList<>();

        while (!partida.isCheckMate()) {
            try {
                UI.limparTela();
                UI.imprimirPartida(partida, capturada);

                System.out.println("Posição de origem: ");
                PosicaoXadrez fonte = UI.lerPosicaoXadrez(sc);

                boolean[][] possiveisMovimentos = partida.possiveisMovimentos(fonte);
                UI.limparTela();
                UI.imprimirTabuleiro(partida.getPecas(), possiveisMovimentos);

                System.out.println();
                System.out.println("Destino: ");
                PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);

                PecaXadrez pecaCapturada = partida.performMovXadrez(fonte, destino);
                if (pecaCapturada != null) capturada.add(pecaCapturada);

                // se há promoção pendente, pede a escolha do jogador
                if (partida.getPromovido() != null) {
                    System.out.println("Peão promovido! Escolha a peça (Q/T/B/C): ");
                    String tipo = sc.nextLine().toUpperCase();
                    while (!tipo.equals("Q") && !tipo.equals("T") && !tipo.equals("B") && !tipo.equals("C")) {
                        System.out.println("Opção inválida! Digite Q, T, B ou C: ");
                        tipo = sc.nextLine().toUpperCase();
                    }
                    partida.substituirPecaPromovida(tipo);
                }

            } catch (ChessExection e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }

        // fim de jogo
        UI.limparTela();
        UI.imprimirPartida(partida, capturada);
        System.out.println("XEQUE-MATE! Vencedor: " + partida.getJogadorAtual());
    }
}