package chess.dao;

import chess.game.ChessGame;

public interface ChessGameDao {
    ChessGame createChessGame();

    ChessGame findCurrentGame();

    boolean hasPlayingGame();

    void updateGame(ChessGame chessGame);

    void removeGame();
}
