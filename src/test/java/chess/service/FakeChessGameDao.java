package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.game.ChessGame;
import chess.game.state.GameState;
import chess.game.state.TerminatedState;
import java.util.Map;

public class FakeChessGameDao implements ChessGameDao {

    private final PieceDao pieceDao = new FakePieceDao();
    private GameState state = null;

    @Override
    public ChessGame createChessGame() {
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        pieceDao.saveAllPieces(chessGame.getPieces());
        updateGame(chessGame);
        return chessGame;
    }

    @Override
    public ChessGame findCurrentGame() {
        if (state == null || state == TerminatedState.getInstance()) {
            return createChessGame();
        }
        Map<Position, Piece> pieces = pieceDao.findPieces();
        return new ChessGame(new Board(pieces), state);
    }

    @Override
    public boolean hasPlayingGame() {
        return state != null && state != TerminatedState.getInstance();
    }

    @Override
    public void updateGame(ChessGame chessGame) {
        pieceDao.saveAllPieces(chessGame.getPieces());
        state = chessGame.getGameState();
    }

    @Override
    public void removeGame() {
        state = null;
    }
}
