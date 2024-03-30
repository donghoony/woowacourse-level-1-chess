package chess.controller;

import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.game.ChessGame;
import chess.game.GameScore;
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
        ChessGame chessGame = gameService.loadPlayingGame();
        executeCommandFromInput(chessGame);
    }

    private void prepareCommandExecutors() {
        executors.put(Command.START, this::loopGame);
        executors.put(Command.MOVE, this::proceedTurn);
        executors.put(Command.STATUS, this::showStatus);
        executors.put(Command.END, this::pause);
        executors.put(Command.RESET, this::reset);
    }

    private void reset(ChessGame chessGame) {
        chessGame.terminate();
        gameService.reset();
    }

    private void executeCommandFromInput(ChessGame chessGame) {
        Command command = inputView.readCommand();
        CommandExecutor commandExecutor = executors.get(command);
        commandExecutor.execute(chessGame);
    }

    public void loopGame(ChessGame chessGame) {
        printBoard(chessGame);
        gameService.startGame(chessGame);
        while (chessGame.isPlaying()) {
            executeCommandFromInput(chessGame);
        }
        gameService.save(chessGame);
        outputView.printEndMessage();
    }

    private void showStatus(ChessGame chessGame) {
        chessGame.validatePlaying();
        GameScore gameScore = chessGame.calculateScore();
        outputView.printScore(gameScore.whiteScore(), gameScore.blackScore());
    }

    private void proceedTurn(ChessGame chessGame) {
        PathDto pathDto = inputView.readPosition();
        Position source = getSourceFrom(pathDto);
        Position destination = getDestinationFrom(pathDto);
        chessGame.proceedTurn(source, destination);
        printBoard(chessGame);
    }

    private void pause(ChessGame chessGame) {
        chessGame.pause();
    }

    private void printBoard(ChessGame chessGame) {
        Map<Position, Piece> pieces = chessGame.getPieces();
        List<RankDisplay> rankDisplays = BoardDisplayConverter.convert(pieces);
        outputView.printBoard(rankDisplays);
    }

    private Position getSourceFrom(PathDto pathDto) {
        return Position.of(
                File.from(pathDto.sourceFileName()),
                Rank.from(pathDto.sourceRankNumber())
        );
    }

    private Position getDestinationFrom(PathDto pathDto) {
        return Position.of(
                File.from(pathDto.destinationFileName()),
                Rank.from(pathDto.destinationRankNumber())
        );
    }
}
