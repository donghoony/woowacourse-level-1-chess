package chess.service;

import chess.dao.PieceDao;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.Map;

public class FakePieceDao implements PieceDao {

    private final Map<Position, Piece> pieces = new HashMap<>();

    @Override
    public void saveAllPieces(Map<Position, Piece> pieces) {
        this.pieces.putAll(pieces);
    }

    @Override
    public Map<Position, Piece> findPieces() {
        return pieces;
    }

    @Override
    public void removeAllPieces() {
        pieces.clear();
    }
}
