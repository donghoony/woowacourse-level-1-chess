package chess.controller;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.game.GameScore;
import chess.game.Name;
import chess.service.GameService;
import chess.view.Command;
import chess.view.InputView;
import chess.view.OutputView;
import chess.view.PathDto;
import chess.view.display.BoardDisplayConverter;
import chess.view.display.RankDisplay;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ChessController {

    private final InputView inputView;
    private final OutputView outputView;

    private final Map<Command, CommandExecutor> executors;
    private final GameService gameService;

    public ChessController(InputView inputView, OutputView outputView, GameService gameService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.gameService = gameService;
        this.executors = new EnumMap<>(Command.class);
    }

    public void run() {
        prepareCommandExecutors();
        outputView.printInitMessage();

        Name name = inputView.readName();
        loopGame(name.asText());
    }

    private void prepareCommandExecutors() {
        executors.put(Command.MOVE, this::proceedTurn);
        executors.put(Command.STATUS, this::showStatus);
        executors.put(Command.END, this::pause);
        executors.put(Command.RESET, this::reset);
    }

    private void executeCommandFromInput(String roomName) {
        Command command = inputView.readCommand();
        CommandExecutor commandExecutor = executors.get(command);
        commandExecutor.execute(roomName);
    }

    public void loopGame(String roomName) {
        printBoard(roomName);
        gameService.getOrCreateChessGame(roomName);
        gameService.resumeGame(roomName);
        while (gameService.isGamePlayingOn(roomName)) {
            executeCommandFromInput(roomName);
        }
        outputView.printEndMessage();
    }

    private void proceedTurn(String roomName) {
        PathDto pathDto = inputView.readPosition();
        Position source = pathDto.toSourcePosition();
        Position destination = pathDto.toDestinationPosition();
        gameService.proceedTurn(roomName, source, destination);
        printBoard(roomName);
    }

    private void showStatus(String roomName) {
        GameScore gameScore = gameService.calculateScore(roomName);
        outputView.printScore(gameScore.whiteScore(), gameScore.blackScore());
    }

    private void pause(String roomName) {
        gameService.pause(roomName);
    }

    private void reset(String roomName) {
        gameService.removeGame(roomName);
    }

    private void printBoard(String roomName) {
        Map<Position, Piece> pieces = gameService.getPieces(roomName);
        List<RankDisplay> rankDisplays = BoardDisplayConverter.convert(pieces);
        outputView.printBoard(rankDisplays);
    }
}
