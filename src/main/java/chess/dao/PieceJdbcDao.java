package chess.dao;

import chess.database.JdbcTemplate;
import chess.database.SingleQueryParameters;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PieceJdbcDao implements PieceDao {

    private final JdbcTemplate<PiecesDto> template;

    public PieceJdbcDao() {
        this.template = new JdbcTemplate<>(resultSet -> new PiecesDto(mapToPieces(resultSet)));
    }

    @Override
    public void saveAllPieces(String name, Map<Position, Piece> pieces) {
        String query = "INSERT INTO Piece (room_name, file, `rank`, type, color) VALUES (?, ?, ?, ?, ?)";
        List<SingleQueryParameters> singleQueryParameters = new ArrayList<>();
        for (Map.Entry<Position, Piece> entry : pieces.entrySet()) {
            singleQueryParameters.add(constructParameters(name, entry));
        }
        removePiecesByName(name);
        template.executeBatch(query, singleQueryParameters);
    }

    private SingleQueryParameters constructParameters(String name, Entry<Position, Piece> entry) {
        Position position = entry.getKey();
        Piece piece = entry.getValue();

        return new SingleQueryParameters(
                name,
                position.getFileName(),
                String.valueOf(position.getRankNumber()),
                PieceMapper.convertToPieceName(piece),
                PieceMapper.convertColor(piece)
        );
    }

    @Override
    public Map<Position, Piece> findPiecesByName(String name) {
        String query = "SELECT file, `rank`, type, color FROM Piece WHERE room_name = ?";
        return template.executeAndRetrieveResult(query, name)
                .map(PiecesDto::pieces)
                .orElseThrow(() -> new IllegalStateException("말을 조회하는 데 실패했습니다."));
    }

    @Override
    public void removePiecesByName(String name) {
        String query = "DELETE FROM Piece WHERE room_name = ?";
        template.execute(query, name);
    }

    private Map<Position, Piece> mapToPieces(ResultSet resultSet) throws SQLException {
        Map<Position, Piece> pieces = new HashMap<>();
        while (resultSet.next()) {
            File file = File.from(resultSet.getString("file"));
            Rank rank = Rank.from(resultSet.getInt("rank"));
            Position position = Position.of(file, rank);
            Piece piece = PieceMapper.mapToPiece(
                    resultSet.getString("color"),
                    resultSet.getString("type")
            );
            pieces.put(position, piece);
        }
        return pieces;
    }
}
