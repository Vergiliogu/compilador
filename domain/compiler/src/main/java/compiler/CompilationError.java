package compiler;

/**
 * Represents an abstract compilation error, either due to lexical or syntactic analysis
 */
public class CompilationError extends Exception {

    private final String errorMessage;
    private final int lineNumber;

    public CompilationError(String errorMessage, int lineNumber) {
        this.errorMessage = errorMessage;
        this.lineNumber = lineNumber;
    }

    public CompilationError(String lexeme, String errorMessage, int lineNumber) {
        this.errorMessage = "%s %s".formatted(lexeme, errorMessage);
        this.lineNumber = lineNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
