package chess.view;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathDtoTest {

    @Test
    @DisplayName("경로 Dto가 출발지 위치를 정확하게 생성한다.")
    void toSourcePositionTest() {
        // given
        PathDto pathDto = new PathDto("a", 1, "b", 2);
        // when
        Position actual = pathDto.toSourcePosition();
        // then
        assertThat(actual).isEqualTo(Position.of(File.A, Rank.ONE));
    }

    @Test
    @DisplayName("경로 Dto가 도착지 위치를 정확하게 생성한다.")
    void toDestinationPositionTest() {
        // given
        PathDto pathDto = new PathDto("a", 1, "b", 2);
        // when
        Position actual = pathDto.toDestinationPosition();
        // then
        assertThat(actual).isEqualTo(Position.of(File.B, Rank.TWO));
    }
}
