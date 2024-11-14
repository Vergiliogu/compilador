package compiler;

/**
 * Runs the semantic analysis.
 */
public class Semantic {

    private static final String HEADER =
    """
    .assembly extern mscorlib {}
    .assembly _codigo_objeto{}
    .module _codigo_objeto.exe
    .class public UNICA{
    .method static public void _principal() {
    .entrypoint
    """;

    private static final String FOOTER =
    """
    ret
    }
    }
    """;

    public void executeAction(int action, Token token)	throws CompilationError {

    }

    /**
     * Once the semantic analysis ran, the object code will output from here.
     */
    public String getObjectCode() {
        return HEADER + "\n" + FOOTER;
    }
}