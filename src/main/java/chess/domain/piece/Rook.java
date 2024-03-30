package chess.domain.piece;

import chess.domain.position.UnitMovement;
import java.util.Map;
import java.util.Set;

public class Rook extends Piece {

    private static final Map<Color, Rook> INSTANCE = Map.of(
            Color.WHITE, new Rook(Color.WHITE),
            Color.BLACK, new Rook(Color.BLACK)
    );

    private static final int MAX_MOVE_STEP = 7;

    private Rook(Color color) {
        super(color,
                Set.of(
                        UnitMovement.differencesOf(1, 0),
                        UnitMovement.differencesOf(0, 1),
                        UnitMovement.differencesOf(-1, 0),
                        UnitMovement.differencesOf(0, -1)
                )
        );
    }

    @Override
    protected boolean isReachable(int step) {
        return step <= MAX_MOVE_STEP;
    }

    public static Rook getInstance(Color color) {
        return INSTANCE.get(color);
    }
}
