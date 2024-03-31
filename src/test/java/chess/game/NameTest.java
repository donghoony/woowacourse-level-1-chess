package chess.game;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @ParameterizedTest
    @ValueSource(strings = {"a", "aaaaabbbbb"})
    @DisplayName("방 이름을 정상적으로 생성한다.")
    void validCreationTest(String value) {
        assertDoesNotThrow(() -> new Name(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"    "})
    @NullAndEmptySource
    @DisplayName("방 이름이 비어있는 경우 예외를 발생한다.")
    void emptyNameTest(String value) {
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방 이름은 빈 값이 될 수 없습니다.");
    }

    @Test
    @DisplayName("방 이름이 10자를 초과하는 경우 예외를 발생한다.")
    void exceedingLengthTest() {
        assertThatThrownBy(() -> new Name("aaaaabbbbba"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방 이름은 10자 이하여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "aA", "a1", "123"})
    @DisplayName("방 이름이 소문자 알파벳이 아닌 경우 예외를 발생한다.")
    void patternMatchTest(String value) {
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방 이름은 소문자 알파벳만 가능합니다.");
    }
}
