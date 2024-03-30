package chess.game.state;

import chess.domain.board.Board;
import chess.domain.position.Position;

public interface GameState {

    GameState start();

    GameState proceedTurn(Board board, Position source, Position destination);

    GameState pause();

    GameState terminate();

    boolean isPlaying();

    default void validatePlaying() {
        if (!isPlaying()) {
            throw new IllegalStateException("게임이 진행되고 있지 않습니다.");
        }
    }
}
