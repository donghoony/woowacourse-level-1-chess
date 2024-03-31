package chess.dao;

import chess.database.DBConnector;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PieceJdbcDao implements PieceDao {

    @Override
    public void saveAllPieces(String name, Map<Position, Piece> pieces) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Piece (room_name, file, `rank`, type, color) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, name);
            for (Map.Entry<Position, Piece> entry : pieces.entrySet()) {
                assignPieceToStatement(statement, entry);
            }
            removePiecesByName(name);
            statement.executeBatch();
        } catch (SQLException e) {
            throw new IllegalStateException("말을 저장하는 데 실패했습니다.");
        }
    }

    private void assignPieceToStatement(PreparedStatement statement, Entry<Position, Piece> entry) throws SQLException {
        Position position = entry.getKey();
        Piece piece = entry.getValue();
        statement.setString(2, position.getFileName());
        statement.setInt(3, position.getRankNumber());
        statement.setString(4, PieceMapper.convertToPieceName(piece));
        statement.setString(5, PieceMapper.convertColor(piece));
        statement.addBatch();
    }

    @Override
    public Map<Position, Piece> findPiecesByName(String name) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT file, `rank`, type, color FROM Piece WHERE room_name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return mapToPieces(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException("말을 조회하는 데 실패했습니다.");
        }
    }

    private Map<Position, Piece> mapToPieces(ResultSet resultSet) throws SQLException {
        Map<Position, Piece> pieces = new HashMap<>();
        while (resultSet.next()) {
            File file = File.from(resultSet.getString("file"));
            Rank rank = Rank.from(resultSet.getInt("rank"));
            Position position = Position.of(file, rank);
            Piece piece = PieceMapper.mapToPiece(resultSet.getString("color"), resultSet.getString("type"));
            pieces.put(position, piece);
        }
        return pieces;
    }

    @Override
    public void removePiecesByName(String name) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Piece WHERE room_name = ?")) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("기물을 삭제하는 데 실패했습니다.");
        }
    }
}
