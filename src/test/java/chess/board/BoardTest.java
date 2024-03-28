package chess.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.piece.Color;
import chess.piece.InitPawn;
import chess.piece.King;
import chess.piece.MovedPawn;
import chess.piece.Piece;
import chess.piece.Rook;
import chess.position.File;
import chess.position.Position;
import chess.position.Rank;
import chess.score.Score;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    @DisplayName("기물을 이동한다.")
    void moveTest() {
        // given
        Map<Position, Piece> pieces = new HashMap<>();
        pieces.put(Position.of(File.A, Rank.ONE), Rook.getInstance(Color.WHITE));
        Board board = new Board(pieces);

        Position source = Position.of(File.A, Rank.ONE);
        Position destination = Position.of(File.A, Rank.TWO);
        // when
        board.move(source, destination, Color.WHITE);
        // then
        Map<Position, Square> squares = board.pieces();
        Square destinationSquare = squares.get(destination);

        assertThat(destinationSquare.getPiece()).isInstanceOf(Rook.class);
    }

    @Test
    @DisplayName("적 기물을 공격한다.")
    void attackTest() {
        // given
        Map<Position, Piece> pieces = new HashMap<>();
        Position source = Position.of(File.A, Rank.ONE);
        Position destination = Position.of(File.A, Rank.TWO);

        pieces.put(source, Rook.getInstance(Color.WHITE));
        pieces.put(destination, Rook.getInstance(Color.BLACK));
        Board board = new Board(pieces);
        // when
        board.move(source, destination, Color.WHITE);
        // then
        Map<Position, Square> squares = board.pieces();
        Square destinationSquare = squares.get(destination);

        assertThat(destinationSquare.hasPieceColored(Color.WHITE)).isTrue();
    }

    @Test
    @DisplayName("전체 판의 점수를 계산한다.")
    void boardScoreTest() {
        // given
        Map<Position, Piece> pieces = Map.of(
                Position.of(File.A, Rank.ONE), Rook.getInstance(Color.WHITE),
                Position.of(File.A, Rank.TWO), Rook.getInstance(Color.WHITE),
                Position.of(File.B, Rank.TWO), MovedPawn.getInstance(Color.BLACK),
                Position.of(File.B, Rank.THREE), InitPawn.getInstance(Color.BLACK)
        );
        Board board = new Board(pieces);
        // when
        Score whiteScore = board.calculateScore(Color.WHITE);
        Score blackScore = board.calculateScore(Color.BLACK);
        // then
        assertAll(
                () -> assertThat(whiteScore.getScore()).isEqualTo(10.0),
                () -> assertThat(blackScore.getScore()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("킹이 잡힌 여부를 올바르게 반환한다.")
    void kingCapturedTest() {
        // given
        Position source = Position.of(File.A, Rank.ONE);
        Position destination = Position.of(File.B, Rank.TWO);
        Map<Position, Piece> pieces = new HashMap<>();
        pieces.put(source, King.getInstance(Color.WHITE));
        pieces.put(destination, King.getInstance(Color.BLACK));

        Board board = new Board(pieces);
        // when
        board.move(source, destination, Color.WHITE);
        boolean actual = board.isKingCaptured(Color.BLACK);
        // then
        assertThat(actual).isTrue();
    }
}
