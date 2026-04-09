package application;

import ai.*;
import chess.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Loop principal unificado que suporta todos os modos de jogo.
 * Substitui o while() do Programa.java original e funciona com
 * qualquer combinação de HumanPlayer / GroqPlayer / GeminiPlayer.
 */
public class GameLoop {

    /** Máximo de jogadas inválidas consecutivas antes de encerrar */
    private static final int MAX_ERROS_CONSECUTIVOS = 5;

    private final GameConfig config;

    public GameLoop(GameConfig config) {
        this.config = config;
    }

    public void iniciar() {
        PartidaXadrez partida  = new PartidaXadrez();
        List<PecaXadrez> capturadas = new ArrayList<>();
        Scanner sc = config.getSc();
        int errosConsecutivos  = 0;

        System.out.println();
        System.out.println("══════════════════════════════════════");
        System.out.println(" Modo: " + config.getMode());
        System.out.println(" Branco: " + config.getJogadorBranco().getNome());
        System.out.println(" Preto : " + config.getJogadorPreto().getNome());
        System.out.println("══════════════════════════════════════");
        System.out.println();

        while (!partida.isCheckMate()) {
            try {
                UI.limparTela();
                UI.imprimirPartida(partida, capturadas);

                AIPlayer jogadorAtual = (partida.getJogadorAtual() == Cor.BRANCO)
                        ? config.getJogadorBranco()
                        : config.getJogadorPreto();

                System.out.println("\n[" + jogadorAtual.getNome() + "] pensando...");

                String boardState = UI.getBoardAsString(partida.getPecas());
                PosicaoXadrez[] jogada = jogadorAtual.getMove(boardState, partida);

                PosicaoXadrez fonte   = jogada[0];
                PosicaoXadrez destino = jogada[1];

                System.out.println("[" + jogadorAtual.getNome() + "] jogou: " + fonte + " → " + destino);

                PecaXadrez capturada = partida.performMovXadrez(fonte, destino);
                if (capturada != null) capturadas.add(capturada);

                // Promoção de peão
                if (partida.getPromovido() != null) {
                    String tipo = resolverPromocao(jogadorAtual, sc);
                    partida.substituirPecaPromovida(tipo);
                }

                errosConsecutivos = 0; // reset ao ter sucesso

            } catch (AIRateLimitException e) {
                System.out.println(UI.ANSI_YELLOW + "[AVISO] " + e.getMessage() + UI.ANSI_RESET);
                sleep(e.getRetryAfterMs());
                // não incrementa erro — é aguardar e continuar

            } catch (AITimeoutException e) {
                errosConsecutivos++;
                System.out.println(UI.ANSI_RED + "[ERRO] " + e.getMessage()
                        + " (" + errosConsecutivos + "/" + MAX_ERROS_CONSECUTIVOS + ")" + UI.ANSI_RESET);
                if (errosConsecutivos >= MAX_ERROS_CONSECUTIVOS) abort("Muitos timeouts consecutivos.");

            } catch (AIBadRequestException e) {
                errosConsecutivos++;
                System.out.println(UI.ANSI_RED + "[ERRO] " + e.getMessage()
                        + " (" + errosConsecutivos + "/" + MAX_ERROS_CONSECUTIVOS + ")" + UI.ANSI_RESET);
                if (errosConsecutivos >= MAX_ERROS_CONSECUTIVOS) abort("Bad requests repetidos — verifique o prompt.");

            } catch (AIAuthException e) {
                // Auth error é fatal — não tem retry que resolva
                abort(e.getMessage());

            } catch (AIException e) {
                errosConsecutivos++;
                System.out.println(UI.ANSI_RED + "[ERRO IA] " + e.getMessage()
                        + " (" + errosConsecutivos + "/" + MAX_ERROS_CONSECUTIVOS + ")" + UI.ANSI_RESET);
                if (errosConsecutivos >= MAX_ERROS_CONSECUTIVOS) abort("Muitos erros consecutivos de IA.");
                sleep(1000);

            } catch (ChessExection e) {
                // Jogada inválida (posição fora do tabuleiro, peça errada, etc.)
                errosConsecutivos++;
                System.out.println(UI.ANSI_RED + "[JOGADA INVÁLIDA] " + e.getMessage()
                        + " (" + errosConsecutivos + "/" + MAX_ERROS_CONSECUTIVOS + ")" + UI.ANSI_RESET);
                if (errosConsecutivos >= MAX_ERROS_CONSECUTIVOS) abort("A IA gerou apenas jogadas inválidas.");
                sleep(500);
            }
        }

        // Fim de jogo
        UI.limparTela();
        UI.imprimirPartida(partida, capturadas);
        System.out.println("\n" + UI.ANSI_GREEN + "╔══════════════════════════════╗" + UI.ANSI_RESET);
        System.out.println(UI.ANSI_GREEN   + "║  XEQUE-MATE! Fim de jogo.   ║" + UI.ANSI_RESET);
        System.out.println(UI.ANSI_GREEN   + "╚══════════════════════════════╝" + UI.ANSI_RESET);
        // getJogadorAtual() aponta para quem foi derrotado após o mate
        Cor vencedorCor = (partida.getJogadorAtual() == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
        AIPlayer vencedor = (vencedorCor == Cor.BRANCO) ? config.getJogadorBranco() : config.getJogadorPreto();
        System.out.println("Vencedor: " + UI.ANSI_YELLOW + vencedor.getNome() + UI.ANSI_RESET);
    }

    // -------------------------------------------------------------------------
    // Promoção de peão
    // -------------------------------------------------------------------------

    private String resolverPromocao(AIPlayer jogador, Scanner sc) {
        // IAs sempre promovem para Rainha (Q) — humanos podem escolher
        if (jogador instanceof HumanPlayer) {
            System.out.println("Peão promovido! Escolha a peça (Q/T/B/C): ");
            String tipo = sc.nextLine().toUpperCase();
            while (!tipo.equals("Q") && !tipo.equals("T") && !tipo.equals("B") && !tipo.equals("C")) {
                System.out.println("Opção inválida! Digite Q, T, B ou C: ");
                tipo = sc.nextLine().toUpperCase();
            }
            return tipo;
        } else {
            System.out.println("[" + jogador.getNome() + "] promoveu peão para Rainha (Q).");
            return "Q";
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static void abort(String motivo) {
        System.out.println(UI.ANSI_RED + "\n[PARTIDA ENCERRADA] " + motivo + UI.ANSI_RESET);
        System.exit(1);
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
