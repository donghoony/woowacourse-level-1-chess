package chess;

import chess.controller.ChessController;
import chess.dao.ChessGameDao;
import chess.dao.ChessGameJdbcDao;
import chess.service.GameService;
import chess.view.InputView;
import chess.view.OutputView;

public class ChessApplication {

    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ChessGameDao chessGameDao = new ChessGameJdbcDao();

        GameService gameService = new GameService(chessGameDao);
        ChessController chessController = new ChessController(inputView, outputView, gameService);
        chessController.run();
    }
}
