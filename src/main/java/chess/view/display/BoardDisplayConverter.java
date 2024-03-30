package chess.view.display;

import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BoardDisplayConverter {

    private BoardDisplayConverter() {
    }

    public static List<RankDisplay> convert(Map<Position, Piece> pieces) {
        return Arrays.stream(Rank.values())
                .map(rank -> convertNotationRankOf(rank, pieces))
                .toList();
    }

    private static RankDisplay convertNotationRankOf(Rank rank, Map<Position, Piece> pieces) {
        List<PieceDisplay> pieceDisplays = Arrays.stream(File.values())
                .map(file -> Position.of(file, rank))
                .map(position -> pieces.getOrDefault(position, null))
                .map(PieceDisplay::getNotationByPiece)
                .toList();
        return new RankDisplay(pieceDisplays);
    }
}
