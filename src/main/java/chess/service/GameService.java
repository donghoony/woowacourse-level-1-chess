package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.ChessGameDto;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.game.ChessGame;
import chess.game.GameScore;
import chess.game.state.InitState;
import java.util.Map;

public class GameService {

    private final ChessGameDao chessGameDao;

    public GameService(ChessGameDao chessGameDao) {
        this.chessGameDao = chessGameDao;
    }

    public ChessGame getOrCreateChessGame(String roomName) {
        return chessGameDao.findGameByName(roomName)
                .map(ChessGameDto::toGame)
                .orElseGet(() -> createChessGame(roomName));
    }

    private ChessGame createChessGame(String roomName) {
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        ChessGameDto chessGameDto = chessGameDao.createChessGame(roomName, chessGame.getPieces(),
                InitState.getInstance());
        return chessGameDto.toGame();
    }

    public void resumeGame(String roomName) {
        ChessGame chessGame = getGameByNameOnExist(roomName);
        if (chessGame.start()) {
            chessGameDao.updateGame(ChessGameDto.from(roomName, chessGame));
        }
    }

    public void proceedTurn(String roomName, Position source, Position destination) {
        ChessGame chessGame = getGameByNameOnPlaying(roomName);
        chessGame.proceedTurn(source, destination);
        chessGameDao.updateGame(ChessGameDto.from(roomName, chessGame));
    }

    public void save(String roomName) {
        ChessGame chessGame = getGameByNameOnExist(roomName);
        chessGameDao.updateGame(ChessGameDto.from(roomName, chessGame));
    }

    public void pause(String roomName) {
        ChessGame chessGame = getGameByNameOnPlaying(roomName);
        chessGame.pause();
        chessGameDao.updateGame(ChessGameDto.from(roomName, chessGame));
    }

    public void removeGame(String roomName) {
        ChessGame chessGame = getGameByNameOnExist(roomName);
        chessGame.terminate();
        chessGameDao.removeGame(roomName);
    }

    public GameScore calculateScore(String roomName) {
        ChessGame chessGame = getGameByNameOnPlaying(roomName);
        return chessGame.calculateScore();
    }

    public boolean isGamePlayingOn(String roomName) {
        return chessGameDao.findGameByName(roomName)
                .map(ChessGameDto::toGame)
                .map(ChessGame::isPlaying)
                .orElse(false);
    }

    public Map<Position, Piece> getPieces(String roomName) {
        ChessGame chessGame = getOrCreateChessGame(roomName);
        return chessGame.getPieces();
    }

    private ChessGame getGameByNameOnPlaying(String roomName) {
        ChessGame chessGame = getGameByNameOnExist(roomName);
        chessGame.validatePlaying();
        return chessGame;
    }

    private ChessGame getGameByNameOnExist(String roomName) {
        return chessGameDao.findGameByName(roomName)
                .map(ChessGameDto::toGame)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임입니다."));
    }
}
