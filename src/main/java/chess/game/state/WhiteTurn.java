package chess.game.state;

import chess.board.Board;
import chess.piece.Color;
import chess.position.Position;

public class WhiteTurn extends TurnState {

    private static final WhiteTurn INSTANCE = new WhiteTurn();

    private WhiteTurn() {
    }

    @Override
    public GameState proceedTurn(Board board, Position source, Position destination) {
        board.move(source, destination, Color.WHITE);
        if (board.isKingCaptured(Color.BLACK)) {
            return TerminatedState.getInstance();
        }
        return BlackTurn.getInstance();
    }

    public static WhiteTurn getInstance() {
        return INSTANCE;
    }
}
