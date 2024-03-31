package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.game.state.GameState;
import java.util.Map;
import java.util.Optional;

public interface ChessGameDao {

    ChessGameDto createChessGame(String name, Map<Position, Piece> pieces, GameState gameState);

    Optional<ChessGameDto> findGameByName(String name);

    void updateGame(ChessGameDto chessGameDto);

    void removeGame(String name);
}
