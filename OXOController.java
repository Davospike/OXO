import OXOExceptions.*;

class OXOController
{
    OXOModel gameModel;
    private int playerTurnCount, matches = 0, turns = 0;

    public OXOController(OXOModel model)
    {
        gameModel = model;
        playerTurnCount = 0;
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
                    if (gameModel.getCurrentPlayer() != gameModel.getPlayerByNumber(gameModel.getNumberOfPlayers()-1)) {
                        playerTurnCount++;
                    } else {
                        playerTurnCount = 0;
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

    private boolean checkWinState(int rows, int columns)
    {
        return (checkWinRow(rows) || checkWinCol(columns) || checkWinDiagonal(rows, columns) ||
                checkWinAntiDiagonal(rows, columns));
    }

    private boolean checkWinRow(int rows)
    {
        while (turns < gameModel.getNumberOfRows()) {
            if (gameModel.getRow(rows).get(turns) != gameModel.getCurrentPlayer()) {
                matches = 0;
            } else if (++matches == gameModel.getWinThreshold()) return true;
            turns++;
        }
        return false;
    }

    private boolean checkWinCol(int columns) {
        while (turns < gameModel.getNumberOfColumns()) {
            if (gameModel.getRow(turns).get(columns) != gameModel.getCurrentPlayer()) {
                matches = 0;
            } else if (++matches == gameModel.getWinThreshold()) return true;
            turns++;
        }
        return false;
    }

    private boolean checkWinDiagonal(int rows, int columns)
    {
        int squareSize = Math.min(gameModel.getNumberOfColumns(), gameModel.getNumberOfRows());

        if (columns == rows) {
            int turns = 0;
            while (turns < squareSize) {
                if (gameModel.getRow(turns).get(turns) == gameModel.getCurrentPlayer()) {
                    if (++matches == gameModel.getWinThreshold()) return true;
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

        if (columns + rows == squareSize - 1) {
            int turns = 0;
            while (turns < squareSize) {
                if (gameModel.getRow(turns).get((squareSize - 1) - turns) == gameModel.getCurrentPlayer()) {
                    if (++matches == gameModel.getWinThreshold()) return true;
                } else {
                    matches = 0;
                }
                turns++;
            }
        }
        return false;
    }

    private boolean checkDrawState()
    {
        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            for (int j = 0; j < gameModel.getNumberOfColumns(); j++) {
                if (gameModel.getCellOwner(i,j) == null) return false;
            }
        }
        return true;
    }
}