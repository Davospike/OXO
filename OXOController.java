import OXOExceptions.*;

import java.util.stream.IntStream;

class OXOController
{
    OXOModel gameModel;
    private int playerTurnCount;
    private final int playerTotal;
    private final int gridSize;
    private final int colMax;
    private final int rowMax;

    public OXOController(OXOModel model)
    {
        gameModel = model;
        playerTurnCount = 0;
        playerTotal = gameModel.getNumberOfPlayers();
        gridSize = gameModel.getNumberOfRows() * gameModel.getNumberOfColumns();
        gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(playerTurnCount));
        colMax = gameModel.getNumberOfColumns();
        rowMax = gameModel.getNumberOfRows();
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        int rows;
        int columns;

        if (!gameModel.isGameDrawn() && gameModel.getWinner() == null) {
            command = command.toLowerCase();
            if (command.length() != 2) {
                throw new InvalidIdentifierLengthException(colMax, rowMax);
            } else if (!Character.isLetter(command.charAt(0))) {
                throw new InvalidIdentifierCharacterException(colMax, rowMax);
            } else if (!Character.isDigit(command.charAt(1))) {
                throw new InvalidIdentifierCharacterException(colMax, rowMax);
            } else {
                rows = Character.getNumericValue(command.charAt(0)) - Character.getNumericValue('a');
                columns = Character.getNumericValue(command.charAt(1)) - 1;
            }
            executeGame(rows, columns);
        }
    }

    private void executeGame (int rows, int columns) throws OXOMoveException
    {
        if (rows < gameModel.getNumberOfRows() && columns < gameModel.getNumberOfColumns()) {
            if (gameModel.getCellOwner(rows, columns) == null) {
                gameModel.setCellOwner(rows, columns, gameModel.getCurrentPlayer());
                if (checkWinState(rows, columns)) {
                    gameModel.setWinner(gameModel.getCurrentPlayer());
                } else if (checkDrawState()) {
                    gameModel.setGameDrawn();
                } else {
                    if (gameModel.getCurrentPlayer() == gameModel.getPlayerByNumber(gameModel.getNumberOfPlayers() - 1)) {
                        playerTurnCount = 0;
                    } else {
                        playerTurnCount++;
                    }
                    gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(playerTurnCount));
                }
            } else {
                throw new CellAlreadyTakenException(rows, columns);
            }
        } else {
            throw new OutsideCellRangeException(rows, columns, colMax, rowMax);
        }
    }

    private boolean checkDrawState()
    {
        return IntStream.range(0, gameModel.getNumberOfRows()).noneMatch(i ->
               IntStream.range(0, gameModel.getNumberOfColumns()).anyMatch(j -> gameModel.getCellOwner(i, j) == null));
    }

    private boolean checkWinState(int rows, int columns)
    {
        return (checkWinRow(rows) || checkWinCol(columns) || checkWinDiagonal(rows, columns) ||
                checkWinAntiDiagonal(rows, columns));
    }

    private boolean checkWinRow(int rows)
    {
        int matches = 0;
        int rowCnt = 0;

        while (rowCnt < gameModel.getNumberOfRows()) {
            if (gameModel.getRow(rows).get(rowCnt) != gameModel.getCurrentPlayer()) {
                matches = 0;
            } else {
                if (++matches == gameModel.getWinThreshold()) {
                    return true;
                }
            }
            rowCnt++;
        }
        return false;
    }

    private boolean checkWinCol(int columns)
    {
        int matches = 0;
        int colCnt = 0;

        while (colCnt < gameModel.getNumberOfColumns()) {
            if (gameModel.getRow(colCnt).get(columns) != gameModel.getCurrentPlayer()) {
                matches = 0;
            } else {
                if (++matches == gameModel.getWinThreshold()) {
                    return true;
                }
            }
            colCnt++;
        }
        return false;
    }

    private boolean checkWinDiagonal(int rows, int columns)
    {
        int squareSize = Math.min(gameModel.getNumberOfColumns(), gameModel.getNumberOfRows());
        int turns, matches = 0;

        if (columns == rows) {
            turns = 0;
            while (turns < squareSize) {
                if (gameModel.getRow(turns).get(turns) == gameModel.getCurrentPlayer()) {
                    if (++matches == gameModel.getWinThreshold()) {
                        return true;
                    }
                } else {
                    matches = 0;
                }
                turns++;
            }
        }
        return false;
    }

    private boolean checkWinAntiDiagonal(int rows, int columns)
    {
        int squareSize = Math.min(gameModel.getNumberOfColumns(), gameModel.getNumberOfRows());
        int turns, matches = 0;

        if (columns + rows == squareSize - 1) {
            turns = 0;
            while (turns < squareSize) {
                if (gameModel.getRow(turns).get((squareSize - 1) - turns) == gameModel.getCurrentPlayer()) {
                    if (++matches == gameModel.getWinThreshold()) {
                        return true;
                    }
                } else {
                    matches = 0;
                }
                turns++;
            }
        }
        return false;
    }
}