package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.game.state.GameState;
import chess.game.state.WhiteTurn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StateMapperTest {

    @Test
    @DisplayName("상태를 문자열로 변환한다.")
    void convertStateTest() {
        // given
        WhiteTurn state = WhiteTurn.getInstance();
        // when
        String stateName = StateMapper.convertToStateName(state);
        // then
        assertThat(stateName).isEqualTo("white");
    }

    @Test
    @DisplayName("문자열을 상태로 매핑한다.")
    void mapToStateTest() {
        // given
        String stateName = "white";
        // when
        GameState state = StateMapper.mapFromName(stateName);
        // then
        assertThat(state).isEqualTo(WhiteTurn.getInstance());
    }
}
