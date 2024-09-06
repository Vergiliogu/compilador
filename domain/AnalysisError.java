public class AnalysisError extends Exception
{
    private int position;
    private String lexeme;

    public AnalysisError(String msg, int position, String lexeme)
    {
        super(msg);
        this.position = position;
        this.lexeme = lexeme;
    }

    public AnalysisError(String msg, String lexeme)
    {
        super(msg);
        this.position = -1;
        this.lexeme = lexeme;
    }

    public int getPosition()
    {
        return position;
    }

    public String getLexeme()
    {
        return lexeme;
    }

    public String toString()
    {
        return super.toString() + ", @ "+position;
    }
}
