package chess.piece;

public class InitPawn extends Pawn {

    private static final int MAX_UNIT_MOVE = 2;

    public InitPawn(Color color) {
        super(color);
    }

    @Override
    public boolean isInitPawn() {
        return true;
    }

    @Override
    protected int getMaxUnitMove() {
        return MAX_UNIT_MOVE;
    }
}
