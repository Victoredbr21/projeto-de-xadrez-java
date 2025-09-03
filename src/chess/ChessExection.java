package chess;

import boardgame.BoardException;

public class ChessExection extends BoardException {
    private static final long serialVersionUID = 1L;
    public ChessExection(String message) {
        super(message);
    }
}
