package chess.domain.score;

import chess.domain.board.Square;
import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.InitPawn;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.MovedPawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import java.util.List;
import java.util.Map;

public class FileScore {

    private static final Score PAWN_SCORE = Score.of(1);
    private static final int MIN_PAWN_COUNT_FOR_MANIPULATION = 2;
    private static final Map<Piece, Score> PIECE_SCORE = Map.ofEntries(
            Map.entry(King.getInstance(Color.BLACK), Score.of(0)),
            Map.entry(Queen.getInstance(Color.BLACK), Score.of(9)),
            Map.entry(Rook.getInstance(Color.BLACK), Score.of(5)),
            Map.entry(Bishop.getInstance(Color.BLACK), Score.of(3)),
            Map.entry(Knight.getInstance(Color.BLACK), Score.of(2.5)),
            Map.entry(MovedPawn.getInstance(Color.BLACK), PAWN_SCORE),
            Map.entry(InitPawn.getInstance(Color.BLACK), PAWN_SCORE),

            Map.entry(King.getInstance(Color.WHITE), Score.of(0)),
            Map.entry(Queen.getInstance(Color.WHITE), Score.of(9)),
            Map.entry(Rook.getInstance(Color.WHITE), Score.of(5)),
            Map.entry(Bishop.getInstance(Color.WHITE), Score.of(3)),
            Map.entry(Knight.getInstance(Color.WHITE), Score.of(2.5)),
            Map.entry(MovedPawn.getInstance(Color.WHITE), PAWN_SCORE),
            Map.entry(InitPawn.getInstance(Color.WHITE), PAWN_SCORE)
    );

    private final List<Square> squares;

    public FileScore(List<Square> squares) {
        this.squares = squares;
    }

    public Score calculateScore(Color color) {
        Score scoreWithoutPawn = calculateScoreWithoutPawn(color);
        Score pawnScore = calculatePawnScore(color);
        return scoreWithoutPawn.add(pawnScore);
    }

    private Score calculateScoreWithoutPawn(Color color) {
        return squares.stream()
                .filter(square -> square.hasPieceColored(color))
                .filter(Square::hasNoPawn)
                .map(Square::getPiece)
                .map(PIECE_SCORE::get)
                .reduce(Score.ZERO, Score::add);
    }

    private Score calculatePawnScore(Color color) {
        int pawnCount = (int) squares.stream()
                .filter(square -> square.hasPieceColored(color))
                .filter(Square::hasPawn)
                .count();
        return manipulateScoreByPawnCount(pawnCount);
    }

    private Score manipulateScoreByPawnCount(int pawnCount) {
        Score pawnScore = PAWN_SCORE.multiplyBy(pawnCount);
        if (pawnCount < MIN_PAWN_COUNT_FOR_MANIPULATION) {
            return pawnScore;
        }
        return pawnScore.divideInHalf();
    }
}
