import OXOExceptions.*;

import java.util.stream.IntStream;

class OXOController
{
    OXOModel gameModel;
    private int playerTurnCount, playerTotal, gridSize;

    public OXOController(OXOModel model)
    {
        gameModel = model;
        playerTurnCount = 0;
        playerTotal = gameModel.getNumberOfPlayers();
        gridSize = gameModel.getNumberOfRows() * gameModel.getNumberOfColumns();
        gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(playerTurnCount));
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        int rows;
        int columns;

        command = command.toLowerCase();

        if (command.length() != 2) {
            throw new OXOMoveException(); // CHANGE TO CORRECT EXCEPTION - invalid identifier
        } else if (!Character.isLetter(command.charAt(0))) {
            throw new OXOMoveException(); // CHANGE TO CORRECT EXCEPTION - invalid identifier character
        } else if (!Character.isDigit(command.charAt(1))) {
            throw new OXOMoveException(); // CHANGE TO CORRECT EXCEPTION - invalid identifier length
        } else {
            rows = Character.getNumericValue(command.charAt(0)) - Character.getNumericValue('a');
            columns = Character.getNumericValue(command.charAt(1)) - 1;
        }

        if (!gameModel.isGameDrawn() || gameModel.getWinner() == null) { // Attempting to make it so no input can be entered once the game is won/tied
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
                throw new OXOMoveException(); // CHANGE TO CORRECT EXCEPTION - cell already taken
            }
        } else {
            throw new OXOMoveException(); // CHANGE TO CORRECT EXCEPTION - outside cell range
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
        int rowCnt = 0, matches = 0;

        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            if (gameModel.getRow(rows).get(i) == gameModel.getCurrentPlayer()) {
                if (++matches == gameModel.getWinThreshold()) return true;
            } else {
                matches = 0;
            }
        }
        return false;
    }

    private boolean checkWinCol(int columns)
    {
        int colCnt = 0, matches = 0;

        for (int i = 0; i < gameModel.getNumberOfColumns(); i++) {
            if (gameModel.getRow(i).get(columns) == gameModel.getCurrentPlayer()) {
                if (++matches == gameModel.getWinThreshold()) return true;
            } else {
                matches = 0;
            }
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