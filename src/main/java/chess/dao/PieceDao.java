package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Map;

public interface PieceDao {

    void saveAllPieces(String name, Map<Position, Piece> pieces);

    Map<Position, Piece> findPiecesByName(String name);

    void removePiecesByName(String name);
}
