package chess.dao;

import chess.database.QueryChain;
import chess.database.RowDataMapper;
import chess.database.RowData;
import chess.database.SingleQueryParameters;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.game.state.GameState;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class ChessGameJdbcDao implements ChessGameDao {

    private final RowDataMapper<ChessGameDto> mapper;

    public ChessGameJdbcDao() {
        mapper = rowData -> {
            String name = rowData.get("room_name");
            QueryChain chain = new QueryChain();
            Map<Position, Piece> pieces = getAllPiecesByName(chain, name);
            GameState gameState = StateMapper.mapFromName(rowData.get("state"));
            chain.commitAndClose();
            return new ChessGameDto(name, pieces, gameState);
        };
    }

    @Override
    public ChessGameDto createChessGame(String name, Map<Position, Piece> pieces, GameState gameState) {
        QueryChain chain = new QueryChain()
                .execute("INSERT INTO ChessGame (room_name, state) VALUES (?, ?)",
                        name, StateMapper.convertToStateName(gameState));
        removeAllPieces(chain, name);
        saveAllPieces(chain, name, pieces);
        chain.commitAndClose();
        return new ChessGameDto(name, pieces, gameState);
    }

    @Override
    public Optional<ChessGameDto> findGameByName(String name) {
        QueryChain chain = new QueryChain()
                .execute("SELECT room_name, state FROM ChessGame WHERE room_name = ?", name);
        Optional<ChessGameDto> result = chain.getResult(mapper);
        chain.commitAndClose();
        return result;
    }

    @Override
    public void updateGame(ChessGameDto chessGameDto) {
        String convertedState = StateMapper.convertToStateName(chessGameDto.gameState());
        QueryChain chain = new QueryChain()
                .execute("UPDATE ChessGame SET state = ? WHERE room_name = ?", convertedState, chessGameDto.name());
        removeAllPieces(chain, chessGameDto.name());
        saveAllPieces(chain, chessGameDto.name(), chessGameDto.pieces());
        chain.commitAndClose();
    }

    @Override
    public void removeGame(String name) {
        QueryChain chain = new QueryChain();
        removeAllPieces(chain, name);
        chain.execute("DELETE FROM ChessGame WHERE room_name = ?", name);
        chain.commitAndClose();
    }

    private void removeAllPieces(QueryChain chain, String name) {
        chain.execute("DELETE FROM Piece WHERE room_name = ?", name);
    }

    private void saveAllPieces(QueryChain chain, String name, Map<Position, Piece> pieces) {
        List<SingleQueryParameters> multipleQueryParameters = pieces.entrySet()
                .stream()
                .map(entry -> constructParameters(name, entry))
                .toList();

        chain.batchExecute(
                "INSERT INTO Piece (room_name, file, `rank`, type, color) VALUES (?, ?, ?, ?, ?)",
                multipleQueryParameters
        );
    }

    private SingleQueryParameters constructParameters(String name, Entry<Position, Piece> entry) {
        Position position = entry.getKey();
        Piece piece = entry.getValue();

        return new SingleQueryParameters(
                name,
                position.getFileName(),
                String.valueOf(position.getRankNumber()),
                PieceMapper.convertToPieceName(piece),
                PieceMapper.convertColor(piece)
        );
    }

    private Map<Position, Piece> getAllPiecesByName(QueryChain chain, String name) {
        return chain.execute("SELECT file, `rank`, type, color FROM Piece WHERE room_name = ?", name)
                .getMapResults(this::mapToPosition, this::mapToPiece);
    }

    private Position mapToPosition(RowData rowData) {
        File file = File.from(rowData.get("file"));
        Rank rank = Rank.from(Integer.parseInt(rowData.get("rank")));
        return Position.of(file, rank);
    }

    private Piece mapToPiece(RowData rowData) {
        return PieceMapper.mapToPiece(rowData.get("color"), rowData.get("type"));
    }
}
