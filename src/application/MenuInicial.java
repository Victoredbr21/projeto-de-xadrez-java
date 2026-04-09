package application;

import ai.*;

import java.util.Scanner;

/**
 * Menu interativo exibido ao iniciar o programa.
 * Permite ao jogador escolher o modo de jogo e as IAs desejadas.
 */
public class MenuInicial {

    private final Scanner sc;

    public MenuInicial(Scanner sc) {
        this.sc = sc;
    }

    public GameConfig exibirMenu() {
        UI.limparTela();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║        XADREZ - MODO DE JOGO         ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Humano vs Humano                 ║");
        System.out.println("║  2. Humano vs IA                     ║");
        System.out.println("║  3. IA vs IA  (Groq vs Gemini)       ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Escolha: ");

        int opcao = lerInt(1, 3);

        switch (opcao) {
            case 1: return configurarHumanoVsHumano();
            case 2: return configurarHumanoVsIA();
            case 3: return configurarIAvsIA();
            default: throw new IllegalStateException("Opção inválida.");
        }
    }

    // -------------------------------------------------------------------------
    // Configurações por modo
    // -------------------------------------------------------------------------

    private GameConfig configurarHumanoVsHumano() {
        AIPlayer branco = new HumanPlayer(sc, "Jogador 1 (Branco)");
        AIPlayer preto  = new HumanPlayer(sc, "Jogador 2 (Preto)");
        return new GameConfig(GameMode.HUMANO_VS_HUMANO, branco, preto, sc);
    }

    private GameConfig configurarHumanoVsIA() {
        System.out.println("\nVocê joga com qual cor?");
        System.out.println("  1. Branco (você começa)");
        System.out.println("  2. Preto");
        System.out.print("Escolha: ");
        int corOpcao = lerInt(1, 2);

        AIPlayer ia = escolherIA();
        if (ia == null) return null; // falha na criação

        HumanPlayer humano = new HumanPlayer(sc, "Humano");

        if (corOpcao == 1) {
            return new GameConfig(GameMode.HUMANO_VS_IA, humano, ia, sc);
        } else {
            return new GameConfig(GameMode.HUMANO_VS_IA, ia, humano, sc);
        }
    }

    private GameConfig configurarIAvsIA() {
        System.out.println("\nConfigurando IA vs IA: Groq (Branco) vs Gemini (Preto)");
        System.out.println("Certifique-se de ter as variáveis GROQ_API_KEY e GEMINI_API_KEY definidas.\n");

        AIPlayer groq   = criarGroq();
        AIPlayer gemini = criarGemini();

        if (groq == null || gemini == null) return null;

        return new GameConfig(GameMode.IA_VS_IA, groq, gemini, sc);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private AIPlayer escolherIA() {
        System.out.println("\nEscolha a IA adversária:");
        System.out.println("  1. Groq  (llama-3.3-70b) — requer GROQ_API_KEY");
        System.out.println("  2. Gemini Flash 2.0      — requer GEMINI_API_KEY");
        System.out.print("Escolha: ");
        int op = lerInt(1, 2);
        return (op == 1) ? criarGroq() : criarGemini();
    }

    private AIPlayer criarGroq() {
        try {
            return new GroqPlayer();
        } catch (AIAuthException e) {
            System.out.println(UI.ANSI_RED + "[ERRO] " + e.getMessage() + UI.ANSI_RESET);
            return null;
        }
    }

    private AIPlayer criarGemini() {
        try {
            return new GeminiPlayer();
        } catch (AIAuthException e) {
            System.out.println(UI.ANSI_RED + "[ERRO] " + e.getMessage() + UI.ANSI_RESET);
            return null;
        }
    }

    private int lerInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.print("Digite um número entre " + min + " e " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número: ");
            }
        }
    }
}
