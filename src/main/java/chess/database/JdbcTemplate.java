package chess.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate<T> {

    private final ResultSetMapper<T> resultSetMapper;

    public JdbcTemplate(ResultSetMapper<T> resultSetMapper) {
        this.resultSetMapper = resultSetMapper;
    }

    public void execute(String query, String... parameters) {
        executeQuery(query, parameters);
    }

    public Optional<T> executeAndRetrieveResult(String query, String... parameters) {
        return executeQuery(query, parameters);
    }

    public void executeBatch(String query, List<SingleQueryParameters> parameters) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            injectBatchParameters(statement, parameters);
            statement.executeBatch();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private Optional<T> executeQuery(String query, String... parameters) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            injectParameters(statement, parameters);
            if (!statement.execute()) {
                return Optional.empty();
            }
            return mapResult(statement.getResultSet());
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private Optional<T> mapResult(ResultSet resultSet) throws SQLException {
        boolean hasNext = resultSet.next();
        if (!hasNext) {
            return Optional.empty();
        }
        return Optional.of(resultSetMapper.mapToObject(resultSet));
    }

    private void injectParameters(PreparedStatement statement, String... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setString(i + 1, parameters[i]);
        }
    }

    private void injectBatchParameters(PreparedStatement statement, List<SingleQueryParameters> parameters) throws SQLException {
        for (SingleQueryParameters queryParameters : parameters) {
            injectParameters(statement, queryParameters.getParameters());
            statement.addBatch();
        }
    }
}
