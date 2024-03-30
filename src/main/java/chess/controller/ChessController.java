package chess.controller;

import chess.domain.board.Square;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.game.ChessGame;
import chess.game.GameScore;
import chess.position.File;
import chess.position.Position;
import chess.position.Rank;
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
    private final ChessGame chessGame;

    public ChessController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.chessGame = new ChessGame(BoardInitializer::createBoard);
        this.executors = new EnumMap<>(Command.class);
    }

    public void run() {
        prepareCommandExecutors();
        outputView.printInitMessage();
        executeCommandFromInput();
    }

    private void prepareCommandExecutors() {
        executors.put(Command.START, this::startGame);
        executors.put(Command.MOVE, this::proceedTurn);
        executors.put(Command.STATUS, this::showStatus);
        executors.put(Command.END, this::terminate);
    }

    private void executeCommandFromInput() {
        Command command = inputView.readCommand();
        CommandExecutor commandExecutor = executors.get(command);
        commandExecutor.execute();
    }

    public void startGame() {
        chessGame.start();
        printBoard();
        while (chessGame.isPlaying()) {
            executeCommandFromInput();
        }
        outputView.printEndMessage();
    }

    private void showStatus() {
        chessGame.validatePlaying();
        GameScore gameScore = chessGame.calculateScore();
        outputView.printScore(gameScore.whiteScore(), gameScore.blackScore());
    }

    private void proceedTurn() {
        PathDto pathDto = inputView.readPosition();
        Position source = getSourceFrom(pathDto);
        Position destination = getDestinationFrom(pathDto);
        chessGame.proceedTurn(source, destination);
        printBoard();
    }

    private void terminate() {
        chessGame.terminate();
    }

    private void printBoard() {
        Map<Position, Square> squares = chessGame.getSquares();
        List<RankDisplay> rankDisplays = BoardDisplayConverter.convert(squares);
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
