package application;

import ai.AIPlayer;

import java.util.Scanner;

/**
 * Agrupa a configuração resultante do menu inicial:
 * modo de jogo, jogador branco, jogador preto e scanner compartilhado.
 */
public class GameConfig {

    private final GameMode mode;
    private final AIPlayer jogadorBranco;
    private final AIPlayer jogadorPreto;
    private final Scanner sc;

    public GameConfig(GameMode mode, AIPlayer jogadorBranco, AIPlayer jogadorPreto, Scanner sc) {
        this.mode          = mode;
        this.jogadorBranco = jogadorBranco;
        this.jogadorPreto  = jogadorPreto;
        this.sc            = sc;
    }

    public GameMode getMode()           { return mode; }
    public AIPlayer getJogadorBranco()  { return jogadorBranco; }
    public AIPlayer getJogadorPreto()   { return jogadorPreto; }
    public Scanner  getSc()             { return sc; }
}
