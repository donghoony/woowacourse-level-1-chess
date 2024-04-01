package chess.database;

import java.util.Map;

public record RowData(Map<String, String> data) {

    public String get(String key) {
        if (!data.containsKey(key)) {
            throw new IllegalArgumentException("존재하지 않는 Column 입니다.");
        }
        return data.get(key);
    }
}
