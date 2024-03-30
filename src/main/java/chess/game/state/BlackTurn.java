package chess.game.state;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.position.Position;

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

    @Override
    public GameState pause() {
        return BlackPausedState.getInstance();
    }

    public static BlackTurn getInstance() {
        return INSTANCE;
    }
}
