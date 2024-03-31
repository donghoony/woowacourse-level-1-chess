package chess.service;

import chess.dao.PieceDao;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.Map;

public class FakePieceDao implements PieceDao {

    private final Map<String, Map<Position, Piece>> fakeDatabase = new HashMap<>();

    @Override
    public void saveAllPieces(String name, Map<Position, Piece> pieces) {
        fakeDatabase.put(name, pieces);
    }

    @Override
    public Map<Position, Piece> findPiecesByName(String name) {
        return fakeDatabase.get(name);
    }

    @Override
    public void removePiecesByName(String name) {
        fakeDatabase.remove(name);
    }
}
