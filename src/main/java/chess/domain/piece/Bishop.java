package chess.domain.piece;

import chess.domain.position.UnitMovement;
import java.util.Map;
import java.util.Set;

public class Bishop extends Piece {

    private static final Map<Color, Bishop> INSTANCE = Map.of(
            Color.WHITE, new Bishop(Color.WHITE),
            Color.BLACK, new Bishop(Color.BLACK)
    );

    private static final int MAX_MOVE_STEP = 7;

    private Bishop(Color color) {
        super(color,
                Set.of(
                        UnitMovement.differencesOf(1, 1),
                        UnitMovement.differencesOf(1, -1),
                        UnitMovement.differencesOf(-1, 1),
                        UnitMovement.differencesOf(-1, -1)
                )
        );
    }

    @Override
    protected boolean isReachable(int step) {
        return step <= MAX_MOVE_STEP;
    }

    public static Bishop getInstance(Color color) {
        return INSTANCE.get(color);
    }
}
