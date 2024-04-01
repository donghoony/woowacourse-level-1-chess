package chess.database;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetMapper<T> {

    T mapToObject(ResultSet resultSet) throws SQLException;
}
