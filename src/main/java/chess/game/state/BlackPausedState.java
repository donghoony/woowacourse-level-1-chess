package chess.game.state;

public class BlackPausedState extends Paused {

    private static final BlackPausedState INSTANCE = new BlackPausedState();

    private BlackPausedState() {
    }

    @Override
    public GameState start() {
        return BlackTurn.getInstance();
    }

    public static GameState getInstance() {
        return INSTANCE;
    }
}
