package chess.domain.piece;

import java.util.Map;

public class MovedPawn extends Pawn {

    private static final Map<Color, MovedPawn> INSTANCE = Map.of(
            Color.WHITE, new MovedPawn(Color.WHITE),
            Color.BLACK, new MovedPawn(Color.BLACK)
    );

    private static final int MAX_MOVE_STEP = 1;

    private MovedPawn(Color color) {
        super(color);
    }

    @Override
    protected boolean isReachable(int step) {
        return step <= MAX_MOVE_STEP;
    }

    public static MovedPawn getInstance(Color color) {
        return INSTANCE.get(color);
    }
}
