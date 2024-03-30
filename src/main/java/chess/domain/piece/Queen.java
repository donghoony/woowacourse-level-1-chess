package chess.domain.piece;

import chess.domain.position.UnitMovement;
import java.util.Map;
import java.util.Set;

public class Queen extends Piece {

    private static final Map<Color, Queen> INSTANCE = Map.of(
            Color.WHITE, new Queen(Color.WHITE),
            Color.BLACK, new Queen(Color.BLACK)
    );

    private static final int MAX_MOVE_STEP = 7;

    private Queen(Color color) {
        super(color,
                Set.of(
                        UnitMovement.differencesOf(1, 1),
                        UnitMovement.differencesOf(1, 0),
                        UnitMovement.differencesOf(1, -1),
                        UnitMovement.differencesOf(0, 1),
                        UnitMovement.differencesOf(0, -1),
                        UnitMovement.differencesOf(-1, 1),
                        UnitMovement.differencesOf(-1, 0),
                        UnitMovement.differencesOf(-1, -1)
                )
        );
    }

    @Override
    protected boolean isReachable(int step) {
        return step <= MAX_MOVE_STEP;
    }

    public static Queen getInstance(Color color) {
        return INSTANCE.get(color);
    }
}
