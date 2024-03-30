package chess.domain.position;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RankTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 9})
    @DisplayName("올바르지 않은 행을 변환하는 경우 예외를 발생한다.")
    void invalidRankNumberTest(int rankNumber) {
        Assertions.assertThatThrownBy(() -> Rank.from(rankNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 행 번호입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 8})
    @DisplayName("범위를 넘어가도록 행을 계산하는 경우 예외를 발생한다.")
    void rankOverflowTest(int difference) {
        Rank rank = Rank.from(1);
        Assertions.assertThatThrownBy(() -> rank.createRankByDifferenceOf(difference))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("행 범위를 벗어납니다.");
    }


    @Test
    @DisplayName("주어진 간격만큼 차이나는 행을 올바르게 생성한다.")
    void createRankByDifferenceTest() {
        // given
        Rank rank = Rank.from(1);
        // when
        Rank actual = rank.createRankByDifferenceOf(5);
        // then
        Assertions.assertThat(actual).isEqualTo(Rank.from(6));
    }

    @Test
    @DisplayName("행의 차이를 계산한다.")
    void subtractTest() {
        // given
        Rank rank = Rank.from(1);
        // when
        int actual = rank.subtract(Rank.from(2));
        // then
        Assertions.assertThat(actual).isEqualTo(-1);
    }
}
