package chess.service;

import chess.dao.ChessGameDao;
import chess.dao.ChessGameDto;
import chess.dao.PieceDao;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.game.state.GameState;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeChessGameDao implements ChessGameDao {

    private final Map<String, GameState> fakeDatabase = new HashMap<>();
    private final PieceDao pieceDao = new FakePieceDao();

    @Override
    public ChessGameDto createChessGame(String name, Map<Position, Piece> pieces, GameState gameState) {
        pieceDao.saveAllPieces(name, pieces);
        ChessGameDto chessGameDto = new ChessGameDto(name, pieces, gameState);
        updateGame(chessGameDto);
        return chessGameDto;
    }

    @Override
    public Optional<ChessGameDto> findGameByName(String name) {
        if (fakeDatabase.containsKey(name)) {
            return Optional.of(new ChessGameDto(name, pieceDao.findPiecesByName(name), fakeDatabase.get(name)));
        }
        return Optional.empty();
    }

    @Override
    public void updateGame(ChessGameDto chessGameDto) {
        fakeDatabase.put(chessGameDto.name(), chessGameDto.gameState());
        pieceDao.saveAllPieces(chessGameDto.name(), chessGameDto.pieces());
    }

    @Override
    public void removeGame(String name) {
        fakeDatabase.remove(name);
        pieceDao.removePiecesByName(name);
    }
}
