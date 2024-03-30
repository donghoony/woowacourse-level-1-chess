package chess.game.state;

import chess.board.Board;
import chess.piece.Color;
import chess.position.Position;

public class BlackTurn extends TurnState {

    private static final BlackTurn INSTANCE = new BlackTurn();

    private BlackTurn() {
    }

    @Override
    public GameState proceedTurn(Board board, Position source, Position destination) {
        board.move(source, destination, Color.BLACK);
        if (board.isKingCaptured(Color.WHITE)) {
            return TerminatedState.getInstance();
        }
        return WhiteTurn.getInstance();
    }

    public static BlackTurn getInstance() {
        return INSTANCE;
    }
}
