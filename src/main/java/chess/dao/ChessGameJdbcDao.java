package chess.dao;

import chess.database.JdbcTemplate;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.game.state.GameState;
import java.util.Map;
import java.util.Optional;

public class ChessGameJdbcDao implements ChessGameDao {

    private final JdbcTemplate<ChessGameDto> template;
    private final PieceDao pieceDao;

    public ChessGameJdbcDao(PieceDao pieceDao) {
        template = new JdbcTemplate<>(resultSet -> new ChessGameDto(
                resultSet.getString("room_name"),
                pieceDao.findPiecesByName(resultSet.getString("room_name")),
                StateMapper.mapFromName(resultSet.getString("state"))
        ));
        this.pieceDao = pieceDao;
    }

    @Override
    public ChessGameDto createChessGame(String name, Map<Position, Piece> pieces, GameState gameState) {
        String query = "INSERT INTO ChessGame (room_name, state) VALUES (?, ?)";
        ChessGameDto chessGameDto = template.executeAndRetrieveResult(
                        query, name, StateMapper.convertToStateName(gameState))
                .orElseThrow(() -> new IllegalArgumentException("게임을 생성하는 데 실패했습니다."));
        pieceDao.saveAllPieces(name, pieces);
        return chessGameDto;
    }

    @Override
    public Optional<ChessGameDto> findGameByName(String name) {
        String query = "SELECT room_name, state FROM ChessGame WHERE room_name = ?";
        return template.executeAndRetrieveResult(query, name);
    }

    @Override
    public void updateGame(ChessGameDto chessGameDto) {
        String convertedState = StateMapper.convertToStateName(chessGameDto.gameState());
        String query = "UPDATE ChessGame SET state = ? WHERE room_name = ?";
        template.execute(query, convertedState, chessGameDto.name());
        pieceDao.saveAllPieces(chessGameDto.name(), chessGameDto.pieces());
    }

    @Override
    public void removeGame(String name) {
        String query = "DELETE FROM ChessGame WHERE room_name = ?";
        template.execute(query, name);
    }
}
