package chess.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QueryChain {

    private final Connection connection;
    private final List<RowData> rows;

    public QueryChain(Connection connection, List<RowData> rows) {
        this.connection = connection;
        this.rows = rows;
    }

    private QueryChain(Connection connection) {
        this(connection, new ArrayList<>());
    }

    public QueryChain() {
        this(DBConnector.getConnection());
    }

    private QueryChain emptyResult() {
        return new QueryChain(connection);
    }

    public QueryChain execute(String query, String... parameters) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            injectParameters(statement, parameters);
            return getNextChainingConnectionWithResult(statement.execute(), statement);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public QueryChain batchExecute(String query, List<SingleQueryParameters> parameters) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            injectBatchParameters(statement, parameters);
            statement.executeBatch();
            return emptyResult();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private QueryChain getNextChainingConnectionWithResult(boolean hasResultSet, PreparedStatement statement)
            throws SQLException {
        if (hasResultSet) {
            return new QueryChain(connection, createRows(statement.getResultSet()));
        }
        return emptyResult();
    }

    private List<RowData> createRows(ResultSet resultSet) throws SQLException {
        List<RowData> databaseRows = new ArrayList<>();
        while (resultSet.next()) {
            databaseRows.add(createRowData(resultSet));
        }
        return databaseRows;
    }

    private RowData createRowData(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<String, String> rowData = new HashMap<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            rowData.put(metaData.getColumnName(i), resultSet.getString(i));
        }
        return new RowData(rowData);
    }

    public <T> Optional<T> getResult(RowDataMapper<T> mapper) {
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.mapToObject(rows.get(0)));
    }

    public <K, V> Map<K, V> getMapResults(RowDataMapper<K> keyMapper, RowDataMapper<V> valueMapper) {
        return rows.stream()
                .collect(
                        HashMap::new,
                        (map, row) -> map.put(keyMapper.mapToObject(row), valueMapper.mapToObject(row)),
                        HashMap::putAll
                );
    }

    public void commitAndClose() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private void injectParameters(PreparedStatement statement, String... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setString(i + 1, parameters[i]);
        }
    }

    private void injectBatchParameters(PreparedStatement statement, List<SingleQueryParameters> parameters)
            throws SQLException {
        for (SingleQueryParameters queryParameters : parameters) {
            injectParameters(statement, queryParameters.getParameters());
            statement.addBatch();
        }
    }
}
