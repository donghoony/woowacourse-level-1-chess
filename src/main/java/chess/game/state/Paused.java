package chess.game.state;

import chess.domain.board.Board;
import chess.domain.position.Position;

public abstract class Paused implements GameState {

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
}
