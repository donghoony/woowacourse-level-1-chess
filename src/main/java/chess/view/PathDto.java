package chess.view;

import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;

public record PathDto(String sourceFileName, int sourceRankNumber,
                      String destinationFileName, int destinationRankNumber) {

    public Position toSourcePosition() {
        File file = File.from(sourceFileName);
        Rank rank = Rank.from(sourceRankNumber);
        return Position.of(file, rank);
    }

    public Position toDestinationPosition() {
        File file = File.from(destinationFileName);
        Rank rank = Rank.from(destinationRankNumber);
        return Position.of(file, rank);
    }
}
