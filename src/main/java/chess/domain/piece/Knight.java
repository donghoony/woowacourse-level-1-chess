package chess.domain.piece;

import chess.domain.position.UnitMovement;
import java.util.Map;
import java.util.Set;

public class Knight extends Piece {

    private static final Map<Color, Knight> INSTANCE = Map.of(
            Color.WHITE, new Knight(Color.WHITE),
            Color.BLACK, new Knight(Color.BLACK)
    );

    private static final int MAX_MOVE_STEP = 1;

    private Knight(Color color) {
        super(color,
                Set.of(
                        UnitMovement.differencesOf(2, 1),
                        UnitMovement.differencesOf(2, -1),
                        UnitMovement.differencesOf(-2, 1),
                        UnitMovement.differencesOf(-2, -1),
                        UnitMovement.differencesOf(1, 2),
                        UnitMovement.differencesOf(1, -2),
                        UnitMovement.differencesOf(-1, 2),
                        UnitMovement.differencesOf(-1, -2)
                ));
    }

    @Override
    protected boolean isReachable(int step) {
        return step <= MAX_MOVE_STEP;
    }

    public static Knight getInstance(Color color) {
        return INSTANCE.get(color);
    }
}
