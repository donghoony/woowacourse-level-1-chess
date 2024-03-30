package chess.domain.board;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.UnitMovement;
import chess.domain.score.FileScore;
import chess.domain.score.Score;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {

    private final Map<Position, Square> squares;

    public Board(Map<Position, Piece> pieces) {
        this.squares = pieces.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        entry -> new Square(entry.getValue()))
                );
    }

    public Square move(Position source, Position destination, Color currentTurnColor) {
        Path path = createPathBetween(source, destination);
        Square destinationSquare = path.traverse(currentTurnColor);
        squares.remove(source);
        squares.put(destination, destinationSquare);
        return destinationSquare;
    }

    private Path createPathBetween(Position source, Position destination) {
        UnitMovement movement = source.unitMovementToward(destination);
        List<Square> routeSquares = Stream.iterate(source,
                        position -> position.isNotEquals(destination),
                        movement::nextPosition)
                .map(this::getSquareByPosition)
                .collect(Collectors.toList());
        routeSquares.add(getSquareByPosition(destination));
        return new Path(routeSquares, movement);
    }

    private Square getSquareByPosition(Position position) {
        return squares.getOrDefault(position, Square.empty());
    }

    public Score calculateScore(Color color) {
        return Arrays.stream(File.values())
                .map(this::getFilePieces)
                .map(fileSquares -> fileSquares.calculateScore(color))
                .reduce(Score.ZERO, Score::add);
    }

    private FileScore getFilePieces(File file) {
        List<Square> filePieces = squares.keySet().stream()
                .filter(position -> position.hasFileOf(file))
                .map(squares::get)
                .toList();
        return new FileScore(filePieces);
    }

    public boolean isKingCaptured(Color color) {
        return squares.values()
                .stream()
                .filter(Square::hasKing)
                .noneMatch(square -> square.hasPieceColored(color));
    }

    public Map<Position, Square> pieces() {
        return Collections.unmodifiableMap(squares);
    }
}
