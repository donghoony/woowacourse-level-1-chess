package chess.view;

import chess.view.display.PieceDisplay;
import chess.view.display.RankDisplay;
import java.util.List;

public class OutputView {

    public void printInitMessage() {
        System.out.printf("""
                        > 체스 게임을 시작합니다.
                        > 게임 시작을 위해 방 이름을 입력하세요, 방 이름은 알파벳 소문자 10글자까지 가능합니다.
                        (추가 커맨드)
                        > 게임 이동 : %s (source) (target), 예: %s b2 b3
                        > 점수 확인 : %s
                        > 게임 종료 : %s (진행 상태가 저장됩니다.)
                        > 방 삭제 : %s (방이 삭제되고 종료됩니다.)
                        > 방 이름 :\s""",
                Command.MOVE.getText(),
                Command.MOVE.getText(),
                Command.STATUS.getText(),
                Command.END.getText(),
                Command.RESET.getText());
    }

    public void printBoard(List<RankDisplay> rankDisplays) {
        rankDisplays.forEach(this::printRank);
    }

    private void printRank(RankDisplay rankDisplay) {
        List<String> notations = rankDisplay.pieceDisplays()
                .stream()
                .map(PieceDisplay::getNotation)
                .toList();
        System.out.println(String.join(" ", notations));
    }

    public void printScore(double whiteScore, double blackScore) {
        System.out.println(" --- 현재 점수 --- ");
        System.out.println("백 : " + whiteScore + "  -  " + blackScore + " 흑");
    }

    public void printEndMessage() {
        System.out.println("게임이 종료되었습니다.");
    }
}
