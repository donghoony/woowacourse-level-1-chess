package chess.game.state;

import chess.domain.board.Board;
import chess.domain.position.Position;

public class InitState implements GameState {

    private static final InitState INSTANCE = new InitState();

    private InitState() {
    }

    @Override
    public GameState start() {
        return WhiteTurn.getInstance();
    }

    @Override
    public GameState proceedTurn(Board board, Position source, Position destination) {
        throw new UnsupportedOperationException("게임이 시작되지 않았습니다.");
    }

    @Override
    public GameState pause() {
        throw new UnsupportedOperationException("게임이 시작되지 않았습니다.");
    }

    @Override
    public GameState terminate() {
        return TerminatedState.getInstance();
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    public static InitState getInstance() {
        return INSTANCE;
    }
}
