package chess.controller;

import chess.game.ChessGame;

public interface CommandExecutor {

    void execute(ChessGame chessGame);
}
