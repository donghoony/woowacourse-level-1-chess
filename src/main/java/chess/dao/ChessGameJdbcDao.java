package chess.dao;

import chess.database.DBConnector;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.game.state.GameState;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class ChessGameJdbcDao implements ChessGameDao {

    private final PieceDao pieceDao;

    public ChessGameJdbcDao(PieceDao pieceDao) {
        this.pieceDao = pieceDao;
    }

    @Override
    public ChessGameDto createChessGame(String name, Map<Position, Piece> pieces, GameState gameState) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO ChessGame (room_name, state) VALUES (?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, StateMapper.convertToStateName(gameState));
            statement.executeUpdate();
            pieceDao.saveAllPieces(name, pieces);
            return new ChessGameDto(name, pieces, gameState);
        } catch (SQLException e) {
            throw new IllegalStateException("게임을 생성하는 데 실패했습니다.");
        }
    }

    @Override
    public Optional<ChessGameDto> findGameByName(String name) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT room_name, state FROM ChessGame WHERE room_name = ?")) {
            statement.setString(1, name);
            return retrieveChessGameFrom(statement.executeQuery());
        } catch (SQLException e) {
            throw new IllegalStateException("게임을 조회하는 데 실패했습니다.");
        }
    }

    private Optional<ChessGameDto> retrieveChessGameFrom(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return Optional.empty();
        }
        String name = resultSet.getString("room_name");
        String state = resultSet.getString("state");
        Map<Position, Piece> pieces = pieceDao.findPiecesByName(name);
        return Optional.of(new ChessGameDto(name, pieces, StateMapper.mapFromName(state)));
    }

    @Override
    public void updateGame(ChessGameDto chessGameDto) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE ChessGame SET state = ? WHERE room_name = ?")) {
            String convertedState = StateMapper.convertToStateName(chessGameDto.gameState());
            statement.setString(1, convertedState);
            statement.setString(2, chessGameDto.name());
            statement.executeUpdate();
            pieceDao.saveAllPieces(chessGameDto.name(), chessGameDto.pieces());
        } catch (SQLException e) {
            throw new IllegalStateException("게임을 저장하는 데 실패했습니다.");
        }
    }

    @Override
    public void removeGame(String name) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM ChessGame WHERE room_name = ?")) {
            pieceDao.removePiecesByName(name);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("게임을 삭제하는 데 실패했습니다.");
        }
    }
}
