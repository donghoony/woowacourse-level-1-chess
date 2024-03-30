package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PieceMapperTest {

    @Test
    @DisplayName("기물의 색을 문자열로 변환한다.")
    void convertColorTest() {
        // given
        Rook rook = Rook.getInstance(Color.WHITE);
        // when
        String color = PieceMapper.convertColor(rook);
        // then
        assertThat(color).isEqualTo("white");
    }

    @Test
    @DisplayName("기물을 문자열로 변환한다.")
    void convertToPieceNameTest() {
        // given
        Rook rook = Rook.getInstance(Color.WHITE);
        // when
        String pieceName = PieceMapper.convertToPieceName(rook);
        // then
        assertThat(pieceName).isEqualTo("rook");
    }

    @Test
    @DisplayName("문자열을 기물로 매핑한다.")
    void mapToPieceTest() {
        // given
        String color = "white";
        String pieceName = "rook";
        // when
        Piece piece = PieceMapper.mapToPiece(color, pieceName);
        // then
        assertThat(piece).isEqualTo(Rook.getInstance(Color.WHITE));
    }
}
