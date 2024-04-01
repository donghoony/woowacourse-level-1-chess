package chess.database;

@FunctionalInterface
public interface RowDataMapper<T> {

    T mapToObject(RowData rowData);
}
