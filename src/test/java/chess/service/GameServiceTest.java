package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.ChessGameDao;
import chess.dao.ChessGameDto;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Rook;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.game.ChessGame;
import chess.game.GameScore;
import chess.game.state.BlackTurn;
import chess.game.state.InitState;
import chess.game.state.WhitePausedState;
import chess.game.state.WhiteTurn;
import java.util.Map;
import java.util.Optional;
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
    @DisplayName("방이 존재하지 않는 경우, 체스 게임을 생성한다.")
    void createChessGameTest() {
        // when
        gameService.getOrCreateChessGame("hello");
        Optional<ChessGameDto> game = chessGameDao.findGameByName("hello");
        // then
        assertThat(game).isPresent();
    }

    @Test
    @DisplayName("방이 이미 존재하는 경우, 해당 방의 체스 게임을 반환한다.")
    void createChessGameOnExistTest() {
        // given
        ChessGame chessGame = new ChessGame(new Board(
                Map.of(Position.of(File.A, Rank.ONE), Rook.getInstance(Color.WHITE))
        ));
        chessGameDao.createChessGame("hello", chessGame.getPieces(), WhiteTurn.getInstance());
        // when
        ChessGame actual = gameService.getOrCreateChessGame("hello");
        // then
        assertThat(actual.getPieces()).containsOnlyKeys(Position.of(File.A, Rank.ONE));
    }

    @Test
    @DisplayName("일시정지된 게임을 시작한다.")
    void resumeTest() {
        // given
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        chessGameDao.createChessGame("hello", chessGame.getPieces(), InitState.getInstance());
        // when
        gameService.resumeGame("hello");
        Optional<ChessGameDto> actual = chessGameDao.findGameByName("hello");
        // then
        assertThat(actual).isPresent();
        assertThat(actual.get().gameState()).isEqualTo(WhiteTurn.getInstance());
    }

    @Test
    @DisplayName("게임을 진행한다.")
    void proceedTurnTest() {
        // given
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        chessGameDao.createChessGame("hello", chessGame.getPieces(), WhiteTurn.getInstance());
        Position source = Position.of(File.A, Rank.TWO);
        Position destination = Position.of(File.A, Rank.THREE);
        // when
        gameService.proceedTurn("hello", source, destination);
        Optional<ChessGameDto> actual = chessGameDao.findGameByName("hello");
        // then
        assertThat(actual).isPresent();
        assertThat(actual.get().pieces()).containsKey(destination);
    }

    @Test
    @DisplayName("게임을 저장한다.")
    void saveTest() {
        // given
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        chessGameDao.createChessGame("hello", chessGame.getPieces(), BlackTurn.getInstance());
        // when
        gameService.save("hello");
        Optional<ChessGameDto> actual = chessGameDao.findGameByName("hello");
        // then
        assertThat(actual).isPresent();
        assertThat(actual.get().gameState()).isEqualTo(BlackTurn.getInstance());
    }

    @Test
    @DisplayName("게임을 일시정지한다.")
    void pauseTest() {
        // given
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        chessGameDao.createChessGame("hello", chessGame.getPieces(), WhiteTurn.getInstance());
        // when
        gameService.pause("hello");
        Optional<ChessGameDto> actual = chessGameDao.findGameByName("hello");
        // then
        assertThat(actual).isPresent();
        assertThat(actual.get().gameState()).isEqualTo(WhitePausedState.getInstance());
    }

    @Test
    @DisplayName("게임을 삭제한다.")
    void removeTest() {
        // given
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        chessGameDao.createChessGame("hello", chessGame.getPieces(), WhiteTurn.getInstance());
        // when
        gameService.removeGame("hello");
        Optional<ChessGameDto> actual = chessGameDao.findGameByName("hello");
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("게임의 점수를 계산한다.")
    void calculateScoreTest() {
        // given
        ChessGame chessGame = new ChessGame(
                new Board(Map.of(Position.of(File.A, Rank.ONE), Rook.getInstance(Color.WHITE)))
        );
        chessGameDao.createChessGame("hello", chessGame.getPieces(), WhiteTurn.getInstance());
        // when
        GameScore actual = gameService.calculateScore("hello");
        // then
        assertThat(actual.whiteScore()).isEqualTo(5);
        assertThat(actual.blackScore()).isZero();
    }

    @Test
    @DisplayName("게임이 진행 중인지 확인한다.")
    void isGamePlayingOnTest() {
        // given
        ChessGame chessGame = new ChessGame(BoardInitializer.createBoard());
        chessGameDao.createChessGame("hello", chessGame.getPieces(), WhiteTurn.getInstance());
        // when
        boolean actual = gameService.isGamePlayingOn("hello");
        // then
        assertThat(actual).isTrue();
    }
}
