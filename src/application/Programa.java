package application;
import boardgame.*;
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
            PartidaXadrez partida = new PartidaXadrez();       // inicia a partida
            List<PecaXadrez> capturada = new ArrayList<>();
            while(true) {//um while para o jogo entrar em loop até o cheque mate finalizar
                try {
                    UI.limparTela();
                    UI.imprimirPartida(partida, capturada);
                    System.out.println("Posicão de origem: ");
                    PosicaoXadrez fonte = UI.lerPosicaoXadrez(sc);     //aqui o usuario vai escolher a peca

                    boolean [][] possiveisMovimentos = partida.possiveisMovimentos(fonte);
                    UI.limparTela();
                    UI.imprimirTabuleiro(partida.getPecas(), possiveisMovimentos); //sobrecarga

                    System.out.println();
                    System.out.println("Destino: ");
                    PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);     //aqui o usuario vai dizer a onde ele quer parar

                    PecaXadrez pecaCapturada = partida.performMovXadrez(fonte, destino);

                    if(pecaCapturada != null) {
                        capturada.add(pecaCapturada);
                    }

                } catch (ChessExection e) {
                    System.out.println(e.getMessage());
                    sc.nextLine();
                }

                catch (InputMismatchException e) {
                    System.out.println(e.getMessage());
                    sc.nextLine();
                }

            }
        } //marcador
    } // marcador

