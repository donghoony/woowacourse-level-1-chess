package chess.view;

import java.util.Arrays;

public enum Command {

    START("start"),
    END("end"),
    ;

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public static Command from(String input) {
        return Arrays.stream(values())
                .filter(command -> command.value.equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 명령어입니다."));
    }

    public boolean isStart() {
        return this == START;
    }
}
