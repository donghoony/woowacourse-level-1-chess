package chess.game.state;

import chess.domain.board.Board;
import chess.domain.position.Position;

public class TerminatedState implements GameState {

    private static final TerminatedState INSTANCE = new TerminatedState();

    private static final String TERMINATED_ERROR_MESSAGE = "게임이 이미 종료되었습니다.";

    private TerminatedState() {
    }

    @Override
    public GameState start() {
        throw new UnsupportedOperationException(TERMINATED_ERROR_MESSAGE);
    }

    @Override
    public GameState proceedTurn(Board board, Position source, Position destination) {
        throw new UnsupportedOperationException(TERMINATED_ERROR_MESSAGE);
    }

    @Override
    public GameState pause() {
        throw new UnsupportedOperationException(TERMINATED_ERROR_MESSAGE);
    }

    @Override
    public GameState terminate() {
        throw new UnsupportedOperationException(TERMINATED_ERROR_MESSAGE);
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    public static TerminatedState getInstance() {
        return INSTANCE;
    }
}
