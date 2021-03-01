package OXOExceptions;

public class InvalidIdentifierCharacterException extends InvalidIdentifierException
{
    private final int colMax;
    private final int rowMax;

    public InvalidIdentifierCharacterException(int colMaximum, int rowMaximum)
    {
        super();
        colMax = colMaximum;
        rowMax = rowMaximum;
    }

    public String toString()
    {
        return this.getClass().getName() + ": You have entered an invalid cell identifier. The current cell range: "+
               "(a-" + (char)(rowMax + 96) + ",1-" + colMax + ")";
    }

}
