package chess.service;

import chess.dao.ChessGameDao;
import chess.game.ChessGame;

public class GameService {

    private final ChessGameDao chessGameDao;

    public GameService(ChessGameDao chessGameDao) {
        this.chessGameDao = chessGameDao;
    }

    public ChessGame createChessGame() {
        return chessGameDao.createChessGame();
    }

    public ChessGame loadPlayingGame() {
        if (chessGameDao.hasPlayingGame()) {
            return chessGameDao.findCurrentGame();
        }
        return createChessGame();
    }

    public void startGame(ChessGame chessGame) {
        if (chessGame.start()) {
            chessGameDao.updateGame(chessGame);
        }
    }

    public void save(ChessGame chessGame) {
        chessGameDao.updateGame(chessGame);
    }

    public ChessGame reset() {
        chessGameDao.removeGame();
        return createChessGame();
    }
}
