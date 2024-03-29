package OXOExceptions;

public class InvalidIdentifierLengthException extends InvalidIdentifierException
{
    private final int colMax;
    private final int rowMax;

    public InvalidIdentifierLengthException(int colMaximum, int rowMaximum)
    {
        super();
        colMax = colMaximum;
        rowMax = rowMaximum;
    }

    public String toString()
    {
        return this.getClass().getName() + ": You must enter two arguments. Please enter a letter between a-" +
                (char)(rowMax + 96) + " followed by a number between 1-" + colMax;
    }

}
