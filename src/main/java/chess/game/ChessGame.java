package chess.game;

import chess.board.Board;
import chess.board.Square;
import chess.game.state.GameState;
import chess.game.state.InitState;
import chess.piece.Color;
import chess.position.Position;
import chess.score.Score;
import java.util.Map;

public class ChessGame {

    private final Board board;
    private GameState gameState;

    public ChessGame(BoardGenerator generator) {
        this.board = generator.createBoard();
        this.gameState = new InitState();
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
