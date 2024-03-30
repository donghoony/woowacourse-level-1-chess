package chess.game.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PausedTest {

    private static class PausedImpl extends Paused {
        @Override
        public GameState start() {
            return null;
        }
    }

    @Test
    @DisplayName("일시정지 상태에서는 움직일 수 없다.")
    void moveTest() {
        // given
        GameState state = new PausedImpl();
        Board board = BoardInitializer.createBoard();
        Position source = Position.of(File.A, Rank.ONE);
        Position destination = Position.of(File.A, Rank.TWO);
        // when, then
        assertThatThrownBy(() -> state.proceedTurn(board, source, destination))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("게임이 시작되지 않았습니다.");
    }

    @Test
    @DisplayName("일시정지 상태에서 종료 시 종료 상태로 전이한다.")
    void terminateTest() {
        // given
        GameState state = new PausedImpl();
        // when
        GameState actual = state.terminate();
        // then
        assertThat(actual).isInstanceOf(TerminatedState.class);
    }

    @Test
    @DisplayName("일시정지 상태에서는 일시정지할 수 없다.")
    void pauseTest() {
        // given
        GameState state = new PausedImpl();
        // when, then
        assertThatThrownBy(state::pause)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("게임이 시작되지 않았습니다.");
    }

    @Test
    @DisplayName("일시정지 상태는 게임 진행 중이 아니다.")
    void isPlayingTest() {
        // given
        GameState state = new PausedImpl();
        // when
        boolean actual = state.isPlaying();
        // then
        assertThat(actual).isFalse();
    }
}
