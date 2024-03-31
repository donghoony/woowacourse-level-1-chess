package chess.game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Name {

    public static final Pattern NAME_PATTERN = Pattern.compile("^[a-z]*$");

    private final String name;

    public Name(String name) {
        validateNotEmpty(name);
        validateLength(name);
        validatePattern(name);
        this.name = name;
    }

    private void validateNotEmpty(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("방 이름은 빈 값이 될 수 없습니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() > 10) {
            throw new IllegalArgumentException("방 이름은 10자 이하여야 합니다.");
        }
    }

    private void validatePattern(String name) {
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("방 이름은 소문자 알파벳만 가능합니다.");
        }
    }

    public String asText() {
        return name;
    }
}
