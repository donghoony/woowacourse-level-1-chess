package chess.domain.piece;

import java.util.Map;

public class InitPawn extends Pawn {

    private static final Map<Color, InitPawn> INSTANCE = Map.of(
            Color.WHITE, new InitPawn(Color.WHITE),
            Color.BLACK, new InitPawn(Color.BLACK)
    );

    private static final int MAX_MOVE_STEP = 2;

    private InitPawn(Color color) {
        super(color);
    }

    @Override
    protected boolean isReachable(int step) {
        return step <= MAX_MOVE_STEP;
    }

    @Override
    public Piece move() {
        return MovedPawn.getInstance(getColor());
    }

    public static InitPawn getInstance(Color color) {
        return INSTANCE.get(color);
    }
}
