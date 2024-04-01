package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Map;

public record PiecesDto(Map<Position, Piece> pieces) {
}
