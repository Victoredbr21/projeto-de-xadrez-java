package application;

import java.util.Scanner;

/**
 * Entry point do sistema de xadrez.
 * Exibe o menu inicial, configura o modo de jogo escolhido
 * e inicia o GameLoop unificado.
 */
public class Programa {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        MenuInicial menu = new MenuInicial(sc);
        GameConfig config = menu.exibirMenu();

        if (config == null) {
            System.out.println("Configuração falhou. Verifique as API keys e tente novamente.");
            System.exit(1);
        }

        GameLoop loop = new GameLoop(config);
        loop.iniciar();

        sc.close();
    }
}
