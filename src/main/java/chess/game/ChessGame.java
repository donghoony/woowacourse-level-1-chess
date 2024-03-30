package chess.game;

import chess.domain.board.Board;
import chess.domain.board.BoardGenerator;
import chess.domain.board.Square;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.domain.score.Score;
import chess.game.state.GameState;
import chess.game.state.InitState;
import java.util.Map;

public class ChessGame {

    private final Board board;
    private GameState gameState;

    public ChessGame(BoardGenerator generator) {
        this.board = generator.createBoard();
        this.gameState = InitState.getInstance();
    }

    public void start() {
        gameState = gameState.start();
    }

    public void proceedTurn(Position source, Position destination) {
        gameState = gameState.proceedTurn(board, source, destination);
    }

    public void terminate() {
        gameState = gameState.terminate();
    }

    public GameScore calculateScore() {
        Score whiteScore = board.calculateScore(Color.WHITE);
        Score blackScore = board.calculateScore(Color.BLACK);
        return new GameScore(whiteScore.getScore(), blackScore.getScore());
    }

    public void validatePlaying() {
        gameState.validatePlaying();
    }

    public boolean isPlaying() {
        return gameState.isPlaying();
    }

    public Map<Position, Square> getSquares() {
        return board.pieces();
    }
}
