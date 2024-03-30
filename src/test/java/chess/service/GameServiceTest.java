package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.ChessGameDao;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.game.ChessGame;
import chess.game.state.WhiteTurn;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameServiceTest {

    private GameService gameService;

    private ChessGameDao chessGameDao;

    @BeforeEach
    void setUp() {
        chessGameDao = new FakeChessGameDao();
        gameService = new GameService(chessGameDao);
    }

    @Test
    @DisplayName("체스 게임을 생성한다.")
    void createChessGameTest() {
        // when
        gameService.createChessGame();
        boolean actual = chessGameDao.hasPlayingGame();
        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("진행 중인 체스 게임이 존재하지 않는 경우, 체스 게임을 새로 만든다.")
    void loadPlayingGameOnNoSavedGameExistsTest() {
        Board board = BoardInitializer.createBoard();
        ChessGame expected = new ChessGame(board);
        // when
        ChessGame chessGame = gameService.loadPlayingGame();
        // then
        assertThat(chessGame.getPieces()).containsAllEntriesOf(expected.getPieces());
    }

    @Test
    @DisplayName("진행 중인 체스 게임이 존재하는 경우, 불러온다.")
    void loadPlayingGameTest() {
        // given
        Board board = new Board(Map.of(
                Position.of(File.A, Rank.ONE), Rook.getInstance(Color.WHITE)
        ));
        ChessGame chessGame = new ChessGame(board);
        chessGameDao.updateGame(chessGame);
        // when
        ChessGame actual = gameService.loadPlayingGame();
        Map<Position, Piece> pieces = actual.getPieces();
        // then
        assertThat(pieces).containsOnlyKeys(Position.of(File.A, Rank.ONE));
    }

    @Test
    @DisplayName("게임을 시작한다.")
    void startGameTest() {
        // given
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        // when
        gameService.startGame(chessGame);
        boolean actual = chessGameDao.hasPlayingGame();
        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("게임을 저장한다.")
    void saveTest() {
        // given
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard(), WhiteTurn.getInstance());
        // when
        gameService.save(chessGame);
        boolean actual = chessGameDao.hasPlayingGame();
        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("게임을 초기화한다.")
    void resetTest() {
        // given
        Board board = new Board(Map.of(
                Position.of(File.A, Rank.ONE), Rook.getInstance(Color.WHITE)
        ));
        ChessGame chessGame = new ChessGame(board);
        chessGameDao.updateGame(chessGame);
        // when
        gameService.reset();
        ChessGame actual = chessGameDao.findCurrentGame();
        // then
        ChessGame expected = new ChessGame(BoardInitializer.createBoard());
        assertThat(actual.getPieces()).containsAllEntriesOf(expected.getPieces());
    }
}
