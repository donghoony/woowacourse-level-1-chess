package chess.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.board.Board;
import chess.piece.Bishop;
import chess.piece.Color;
import chess.piece.Rook;
import chess.position.File;
import chess.position.Position;
import chess.position.Rank;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessGameTest {

    @Test
    @DisplayName("최초 생성 시에는 플레이 중이 아니다.")
    void initIsPlayingTest() {
        // given
        ChessGame chessGame = new ChessGame( () -> new Board(Map.of()));
        // when
        boolean actual = chessGame.isPlaying();
        // then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("시작한 뒤에는 플레이 중으로 변화한다.")
    void isPlayingAfterStartTest() {
        // given
        ChessGame chessGame = new ChessGame( () -> new Board(Map.of()));
        // when
        chessGame.start();
        boolean actual = chessGame.isPlaying();
        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("점수를 올바르게 계산한다.")
    void calculateScoreTest() {
        // given
        ChessGame chessGame = new ChessGame(() -> new Board(
                Map.of(
                        Position.of(File.A, Rank.ONE), Bishop.getInstance(Color.BLACK),
                        Position.of(File.A, Rank.TWO), Rook.getInstance(Color.WHITE)
                )
        ));
        // when
        GameScore actual = chessGame.calculateScore();
        // then
        assertAll(
                () -> assertThat(actual.blackScore()).isEqualTo(3),
                () -> assertThat(actual.whiteScore()).isEqualTo(5)
        );
    }
}
