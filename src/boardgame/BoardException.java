package boardgame;

import chess.PecaXadrez;

public class BoardException extends RuntimeException {
private static final long serialVersionUID = 1L;

//construtor
    public BoardException(String msg) {
        super(msg);
    }
} //marcador
