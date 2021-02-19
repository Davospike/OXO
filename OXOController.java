import OXOExceptions.*;

class OXOController
{
    OXOModel gameModel;
    int playerTurnCount;

    public OXOController(OXOModel model)
    {
        gameModel = model;
        playerTurnCount = 0;

        gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(playerTurnCount));
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        if (command.length() != 2 || !Character.isLetter(command.charAt(0)) || !Character.isDigit(command.charAt(1))) {
            System.out.println("Error: Invalid argument");
        }
        int rows = Character.getNumericValue(command.charAt(0)) - Character.getNumericValue('a');
        int columns = Character.getNumericValue(command.charAt(1)) - 1;

        if (rows >= gameModel.getNumberOfRows() || columns >= gameModel.getNumberOfColumns()) {
            System.out.println("Error: Invalid cell");
        }
        if (gameModel.getCellOwner(rows, columns) != null) {
            System.out.println("Error: Cell taken");
        }

        gameModel.setCellOwner(rows, columns, gameModel.getCurrentPlayer());

        if (checkWinState(rows, columns)) {
            gameModel.setWinner(gameModel.getCurrentPlayer());
        }
        if (checkDrawState()) {
            gameModel.setGameDrawn();
        }
        nextPlayer();

    }

    private void nextPlayer()
    {
        if (gameModel.getCurrentPlayer() != gameModel.getPlayerByNumber(gameModel.getNumberOfPlayers()-1)) {
            playerTurnCount++;
        } else {
            playerTurnCount = 0;
        }
        gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(playerTurnCount));
    }

    // CHANGE BELOW !!!!!!!!!!!!!!!!!!!!!!!!!!

    private boolean checkWinState(int row, int col)
    {
        int matches;
        int squareSize;

        //Check Rows
        matches = 0;
        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            if (gameModel.getRow(row).get(i) == gameModel.getCurrentPlayer()) {
                if (++matches == gameModel.getWinThreshold()) return true;
            } else {
                matches = 0;
            }
        }

        //Check Columns
        matches = 0;
        for (int i = 0; i < gameModel.getNumberOfColumns(); i++) {
            if (gameModel.getRow(i).get(col) == gameModel.getCurrentPlayer()) {
                if (++matches == gameModel.getWinThreshold()) return true;
            } else {
                matches = 0;
            }
        }

        //Check Diagonal
        squareSize = Math.min(gameModel.getNumberOfColumns(),gameModel.getNumberOfRows());
        matches = 0;
        if (col == row) {
            for (int i = 0; i < squareSize; i++) {
                if (gameModel.getRow(i).get(i) == gameModel.getCurrentPlayer()) {
                    if (++matches == gameModel.getWinThreshold()) return true;
                } else {
                    matches = 0;
                }
            }
        }

        //Check Anti-Diagonal
        matches = 0;
        if (col+row == squareSize-1) {
            for (int i = 0; i < squareSize; i++) {
                if (gameModel.getRow(i).get((squareSize-1)-i) == gameModel.getCurrentPlayer()) {
                    if (++matches == gameModel.getWinThreshold()) return true;
                } else {
                    matches = 0;
                }
            }
        }
        return false;
    }

    private boolean checkDrawState()
    {
        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            for (int j = 0; j < gameModel.getNumberOfColumns(); j++) {
                if (gameModel.getCellOwner(i,j)==null) return false;
            }
        }

        return true;
    }
}