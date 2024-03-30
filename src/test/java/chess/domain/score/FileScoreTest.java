package chess.domain.score;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.board.Square;
import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.MovedPawn;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileScoreTest {

    private static final List<Square> squares = List.of(
            new Square(King.getInstance(Color.WHITE)),
            new Square(Queen.getInstance(Color.WHITE)),
            new Square(Knight.getInstance(Color.WHITE)),
            new Square(MovedPawn.getInstance(Color.WHITE)),
            new Square(Rook.getInstance(Color.BLACK)),
            new Square(Bishop.getInstance(Color.BLACK)),
            new Square(MovedPawn.getInstance(Color.BLACK)),
            new Square(MovedPawn.getInstance(Color.BLACK))
    );

    @Test
    @DisplayName("주어진 기물들의 점수를 색에 따라 올바르게 계산한다.")
    void calculatePieceScoreByColorTest() {
        // given
        FileScore fileSquares = new FileScore(squares);
        // when
        Score whiteScore = fileSquares.calculateScore(Color.WHITE);
        Score blackScore = fileSquares.calculateScore(Color.BLACK);
        // then
        assertAll(
                () -> assertThat(whiteScore).isEqualTo(Score.of(12.5)),
                () -> assertThat(blackScore).isEqualTo(Score.of(9))
        );
    }

    @Test
    @DisplayName("폰이 여러 개 있는 경우, 점수를 조절한다.")
    void manipulatePawnScoreTest() {
        // given
        List<Square> pawns = List.of(
                new Square(MovedPawn.getInstance(Color.BLACK)),
                new Square(MovedPawn.getInstance(Color.BLACK)),
                new Square(MovedPawn.getInstance(Color.BLACK))
        );
        FileScore fileSquares = new FileScore(pawns);
        // when
        Score actual = fileSquares.calculateScore(Color.BLACK);
        // then
        assertThat(actual).isEqualTo(Score.of(1.5));
    }

    @Test
    @DisplayName("폰이 한 개만 있는 경우 올바르게 점수를 계산한다.")
    void calculateSinglePawnTest() {
        // given
        List<Square> pawns = List.of(
                new Square(MovedPawn.getInstance(Color.BLACK))
        );
        FileScore fileSquares = new FileScore(pawns);
        // when
        Score actual = fileSquares.calculateScore(Color.BLACK);
        // then
        assertThat(actual).isEqualTo(Score.of(1));
    }
}
