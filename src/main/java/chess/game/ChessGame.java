package chess.game;

import chess.domain.board.Board;
import chess.domain.board.Square;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.score.Score;
import chess.game.state.GameState;
import chess.game.state.InitState;
import java.util.HashMap;
import java.util.Map;

public class ChessGame {

    private final Board board;
    private GameState gameState;

    public ChessGame(Board board) {
        this(board, InitState.getInstance());
    }

    public ChessGame(Board board, GameState gameState) {
        this.board = board;
        this.gameState = gameState;
    }

    public boolean start() {
        if (gameState.isPlaying()) {
            return false;
        }
        gameState = gameState.start();
        return true;
    }

    public void proceedTurn(Position source, Position destination) {
        gameState = gameState.proceedTurn(board, source, destination);
    }

    public void pause() {
        gameState = gameState.pause();
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

    public Map<Position, Piece> getPieces() {
        Map<Position, Square> pieces = board.pieces();
        return pieces.keySet()
                .stream()
                .collect(
                        HashMap::new,
                        (map, position) -> map.put(position, pieces.get(position).getPiece()),
                        HashMap::putAll
                );
    }

    public GameState getGameState() {
        return gameState;
    }
}
