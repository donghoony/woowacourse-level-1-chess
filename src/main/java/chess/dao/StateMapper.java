package chess.dao;

import chess.game.state.BlackPausedState;
import chess.game.state.BlackTurn;
import chess.game.state.GameState;
import chess.game.state.InitState;
import chess.game.state.TerminatedState;
import chess.game.state.WhitePausedState;
import chess.game.state.WhiteTurn;
import java.util.Arrays;

public enum StateMapper {

    INIT(InitState.getInstance(), "init"),
    WHITE_TURN(WhiteTurn.getInstance(), "white"),
    BLACK_TURN(BlackTurn.getInstance(), "black"),
    TERMINATED(TerminatedState.getInstance(), "terminated"),
    WHITE_PAUSED(WhitePausedState.getInstance(), "white_paused"),
    BLACK_PAUSED(BlackPausedState.getInstance(), "black_paused");

    private final GameState state;
    private final String name;

    StateMapper(GameState state, String name) {
        this.state = state;
        this.name = name;
    }

    public static GameState mapFromName(String name) {
        return Arrays.stream(values())
                .filter(stateMapper -> stateMapper.hasNameOf(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상태입니다."))
                .state;
    }

    public static String convertToStateName(GameState state) {
        return Arrays.stream(values())
                .filter(stateMapper -> stateMapper.state == state)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상태입니다."))
                .name;
    }

    private boolean hasNameOf(String name) {
        return this.name.equals(name);
    }
}
