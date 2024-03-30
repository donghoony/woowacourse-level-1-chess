package chess.game.state;

public class WhitePausedState extends Paused {

    public static final WhitePausedState INSTANCE = new WhitePausedState();

    private WhitePausedState() {
    }

    @Override
    public GameState start() {
        return WhiteTurn.getInstance();
    }

    public static GameState getInstance() {
        return INSTANCE;
    }
}
