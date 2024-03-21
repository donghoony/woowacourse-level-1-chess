package chess.board;

import chess.piece.Piece;
import chess.position.Direction;
import chess.position.Position;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Path {

    private final List<Position> positions;

    private Path(List<Position> positions) {
        this.positions = positions;
    }

    public static Path createExcludingBothEnds(Position source, Position destination) {
        Direction direction = Direction.calculateBetween(source, destination);
        if (direction == Direction.KNIGHT) {
            return new Path(Collections.emptyList());
        }
        List<Position> positions = getPositionsBetween(source, destination, direction);
        return new Path(positions);
    }

    private static List<Position> getPositionsBetween(Position source, Position destination, Direction direction) {
        return Stream.iterate(
                        direction.nextPosition(source),
                        position -> position.isNotEquals(destination),
                        direction::nextPosition)
                .toList();
    }

    public boolean hasPieceOnRoute(Map<Position, Piece> pieces) {
        return positions.stream()
                .anyMatch(pieces::containsKey);
    }
}
