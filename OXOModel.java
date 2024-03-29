import java.util.*;

class OXOModel
{
    private final ArrayList<ArrayList<OXOPlayer>> cells;
    private final ArrayList<OXOPlayer> players;
    private OXOPlayer currentPlayer;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh)
    {
        setWinThreshold(winThresh);
        cells = new ArrayList<>();

        for (int i = 0; i < numberOfRows; i++) {
            ArrayList<OXOPlayer> newCell = new ArrayList<>(numberOfColumns);
            for (int j = 0; j < numberOfColumns; j++) {
                newCell.add(null);
            }
            cells.add(newCell);
        }
        players = new ArrayList<>();
    }

    public int getNumberOfPlayers()
    {
        return players.size();
    }

    public void addPlayer(OXOPlayer player)
    {
        players.add(player);
    }

    public OXOPlayer getPlayerByNumber(int number)
    {
        return players.get(number);
    }

    public OXOPlayer getWinner()
    {
        return winner;
    }

    public void setWinner(OXOPlayer player)
    {
        winner = player;
    }

    public OXOPlayer getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer(OXOPlayer player)
    {
        currentPlayer = player;
    }

    public int getNumberOfRows()
    {
        return cells.size();
    }

    public ArrayList<OXOPlayer> getRow(int rowNumber)
    {
        return cells.get(rowNumber);
    }

    public int getNumberOfColumns()
    {
        int cnt = 0;

        for (ArrayList<OXOPlayer> cell : cells) {
            if (cell.size() > cnt) {
                cnt = cell.size();
            }
        }
        return cnt;
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber)
    {
        if ((rowNumber > this.getNumberOfRows() - 1 || (colNumber > cells.get(rowNumber).size() - 1))) {
            return null;
        } else {
            return cells.get(rowNumber).get(colNumber);
        }
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player)
    {
        cells.get(rowNumber).set(colNumber, player);
    }

    public void setWinThreshold(int winThresh)
    {
        winThreshold = winThresh;
    }

    public int getWinThreshold()
    {
        return winThreshold;
    }

    public void setGameDrawn()
    {
        gameDrawn = true;
    }

    public boolean isGameDrawn()
    {
        return gameDrawn;
    }
}