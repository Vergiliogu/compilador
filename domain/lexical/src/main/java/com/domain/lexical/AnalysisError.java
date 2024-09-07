package com.domain.lexical;

public class AnalysisError extends Exception {

    private final int lineNumber;
    private final String lexeme;

    public AnalysisError(String msg, int lineNumber, String lexeme) {
        super(msg);
        this.lineNumber = lineNumber;
        this.lexeme = lexeme;
    }

    public AnalysisError(String msg, String lexeme) {
        super(msg);
        this.lineNumber = -1;
        this.lexeme = lexeme;
    }

    public int getPosition() {
        return lineNumber;
    }

    public String getLexeme() {
        return lexeme;
    }
}
