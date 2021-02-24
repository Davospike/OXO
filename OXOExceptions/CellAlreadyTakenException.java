package OXOExceptions;

public class CellAlreadyTakenException extends OXOMoveException
{
    private final int row;
    private final int column;

    public CellAlreadyTakenException(int rows, int columns)
    {
        row = rows;
        column = columns;
    }

    public String toString()
    {
        return this.getClass().getName() + ": Grid point (" + (char)(row + 97) + "," + (column + 1) + ") has already" +
                " been claimed";
    }

}
