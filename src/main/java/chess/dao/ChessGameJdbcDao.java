package chess.dao;

import chess.database.DBConnector;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.game.ChessGame;
import chess.game.state.TerminatedState;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ChessGameJdbcDao implements ChessGameDao {

    private final PieceDao pieceDao;

    public ChessGameJdbcDao(PieceDao pieceDao) {
        this.pieceDao = pieceDao;
    }

    @Override
    public ChessGame createChessGame() {
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO chess.ChessGame (state) VALUES (?)")) {
            statement.setString(1, StateMapper.convertToStateName(chessGame.getGameState()));
            statement.executeUpdate();
            pieceDao.saveAllPieces(chessGame.getPieces());
            return chessGame;
        } catch (SQLException e) {
            throw new IllegalStateException("게임을 생성하는 데 실패했습니다.");
        }
    }

    @Override
    public ChessGame findCurrentGame() {
        Map<Position, Piece> pieces = pieceDao.findPieces();
        Board board = new Board(pieces);

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT state FROM chess.ChessGame")) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String state = resultSet.getString("state");
            return new ChessGame(board, StateMapper.mapFromName(state));
        } catch (SQLException e) {
            throw new IllegalStateException("게임을 조회하는 데 실패했습니다.");
        }
    }

    @Override
    public boolean hasPlayingGame() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT state FROM chess.ChessGame WHERE state != ?")) {
            statement.setString(1, StateMapper.convertToStateName(TerminatedState.getInstance()));
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new IllegalStateException("게임을 조회하는 데 실패했습니다.");
        }
    }

    @Override
    public void updateGame(ChessGame chessGame) {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE chess.ChessGame SET state = ?")) {
            String convertedState = StateMapper.convertToStateName(chessGame.getGameState());
            statement.setString(1, convertedState);
            statement.executeUpdate();
            pieceDao.saveAllPieces(chessGame.getPieces());
        } catch (SQLException e) {
            throw new IllegalStateException("게임을 저장하는 데 실패했습니다.");
        }
    }

    @Override
    public void removeGame() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM chess.ChessGame")) {
            statement.executeUpdate();
            pieceDao.removeAllPieces();
        } catch (SQLException e) {
            throw new IllegalStateException("게임을 삭제하는 데 실패했습니다.");
        }
    }
}
