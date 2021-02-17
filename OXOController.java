import OXOExceptions.*;

class OXOController
{
    private OXOModel model;
    private int playerTurns = 0;
    private int rowCnt;
    private int colCnt;
    private int winThreshhold;

    public OXOController(OXOModel model)
    {
        this.model = model;
        model.setCurrentPlayer(model.getPlayerByNumber(0));
        rowCnt = model.getNumberOfRows();
        colCnt = model.getNumberOfColumns();
        winThreshhold = model.getWinThreshold();

        if (winThreshhold > rowCnt && winThreshhold > colCnt) {
            System.out.println("Error: Win threshold is larger than grid");
            System.exit(0);
        }
    }



    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        if (model.getWinner() != null || model.isGameDrawn()) return;
        if (command.length() == 2) {
            int x = command.toLowerCase().charAt(0) - 'a';
            int y = command.charAt(1) - '0' - 1;

            if (checkValidInput(x, y)) {
                playerTurns++;
                model.setCellOwner(x, y, model.getCurrentPlayer());
                checkWinConditions();
                model.setCurrentPlayer(model.getPlayerByNumber(playerTurns % model.getNumberOfPlayers()));
            }
        } else {
            throw new OXOMoveException();
        }
    }

    public boolean checkValidInput(int x, int y) throws OXOMoveException
    {
        if (!checkBounds(x, y)) {
            throw new OXOMoveException(x, y);
        } else if (model.getCellOwner(x, y) != null) {
            throw new OXOMoveException(x, y);
        } else {
            return true;
        }
    }

    private boolean checkBounds(int x, int y)
    {
        return (x >= 0 && x < rowCnt) && (y >= 0 && y < colCnt);
    }

    public void checkWinConditions() {
        OXOPlayer currPlayer = model.getCurrentPlayer();

        if (checkLines(colCnt, rowCnt, 1, 0) || checkLines(rowCnt, colCnt, 0, 1)) {
            model.setWinner(currPlayer);
            return;
        }
        if (checkDiagonal(-1) || checkDiagonal(1)) {
            model.setWinner(currPlayer);
            return;
        }
        if (playerTurns == rowCnt * colCnt) {
            model.setGameDrawn();
        }
    }

    public boolean checkLines(int dir1, int dir2, int colCheck, int rowCheck)
    {
        OXOPlayer currPlayer = model.getCurrentPlayer();

        for (int i = 0; i < dir1; i++) {
            int sum = 0;
            for (int j = 0; j < dir2; j++) {
                if (model.getCellOwner(j * colCheck + i * rowCheck, i * colCheck + j * rowCheck) == currPlayer) {
                    sum++;
                } else {
                    sum = 0;
                }
                if (sum == winThreshhold) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDiagonal(int multi)
    {
        OXOPlayer currPlayer = model.getCurrentPlayer();

        for (int i = 0; i < rowCnt; i++) {
            for (int j = 0; j < colCnt; j++) {
                if (model.getCellOwner(i, j) == currPlayer) {
                    int sum = 1;
                    boolean check = false;

                    while (!check) {
                        if (checkBounds(i + sum, j + (sum * multi))) {
                            if (model.getCellOwner(i + sum, j + (sum * multi)) == currPlayer) {
                                sum++;
                            } else {
                                check = true;
                            }
                            if (sum == winThreshhold) {
                                return true;
                            }
                        } else {
                            check = true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
