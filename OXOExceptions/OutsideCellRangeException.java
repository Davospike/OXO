package OXOExceptions;

public class OutsideCellRangeException extends CellDoesNotExistException
{
    private final int row;
    private final int column;
    private final int colMax;
    private final int rowMax;

    public OutsideCellRangeException(int rows, int columns, int colMaximum, int rowMaximum)
    {
        super();
        row = rows;
        column = columns;
        colMax = colMaximum;
        rowMax = rowMaximum;
    }

    public String toString()
    {
        return this.getClass().getName() + ": Grid point (" + (char)(row + 97) + "," + (column + 1) + ") is outside of"
                + " the current cell range: (a-" + (char)(rowMax + 96) + ",1-" + colMax + ")";
    }

}
