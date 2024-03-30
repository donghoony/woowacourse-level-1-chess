package chess.domain.piece;

import chess.domain.position.UnitMovement;
import java.util.Set;

public abstract class Piece {

    private final Color color;
    private final Set<UnitMovement> unitMovements;

    protected Piece(Color color, Set<UnitMovement> unitMovements) {
        this.color = color;
        this.unitMovements = unitMovements;
    }

    public boolean isMovable(UnitMovement movement, int step) {
        return unitMovements.contains(movement) &&
                isReachable(step);
    }

    public Piece move() {
        return this;
    }

    protected abstract boolean isReachable(int step);

    public boolean canAttack(UnitMovement movement, int step) {
        return isMovable(movement, step);
    }

    public boolean hasSameColorWith(Piece piece) {
        return color == piece.color;
    }

    public boolean hasDifferentColorWith(Piece piece) {
        return !hasSameColorWith(piece);
    }

    public boolean hasColorOf(Color color) {
        return this.color == color;
    }

    public boolean hasOpponentColorOf(Color color) {
        return this.color != color;
    }

    public boolean isPawn() {
        return false;
    }

    public boolean isNotPawn() {
        return !isPawn();
    }

    public boolean isKing() {
        return false;
    }

    public Color getColor() {
        return color;
    }
}
