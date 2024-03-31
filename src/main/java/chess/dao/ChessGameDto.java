package chess.dao;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.game.ChessGame;
import chess.game.state.GameState;
import java.util.Map;

public record ChessGameDto(String name, Map<Position, Piece> pieces, GameState gameState) {

    public static ChessGameDto from(String name, ChessGame chessGame) {
        return new ChessGameDto(name, chessGame.getPieces(), chessGame.getGameState());
    }

    public ChessGame toGame() {
        return new ChessGame(new Board(pieces), gameState);
    }
}
