package chess.game.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BlackPausedStateTest {

    @Test
    @DisplayName("시작하면 흑색 턴으로 전이한다.")
    void resumeTest() {
        // given
        GameState state = BlackPausedState.getInstance();
        // when
        GameState actual = state.start();
        // then
        assertThat(actual).isEqualTo(BlackTurn.getInstance());
    }
}
