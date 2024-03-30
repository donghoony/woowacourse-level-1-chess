package chess.domain.piece;

import chess.domain.position.UnitMovement;
import java.util.Map;
import java.util.Set;

public class King extends Piece {

    private static final Map<Color, King> INSTANCE = Map.of(
            Color.WHITE, new King(Color.WHITE),
            Color.BLACK, new King(Color.BLACK)
    );

    private static final int MAX_UNIT_MOVE = 1;

    private King(Color color) {
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
        return step <= MAX_UNIT_MOVE;
    }

    @Override
    public boolean isKing() {
        return true;
    }

    public static King getInstance(Color color) {
        return INSTANCE.get(color);
    }
}
