package com.domain.lexical;

public class LexicalError extends AnalysisError {

    public LexicalError(String msg, int lineNumber, String lexeme) {
        super(msg, lineNumber, lexeme);
    }

    public LexicalError(String msg, String lexeme) {
        super(msg, lexeme);
    }

    public LexicalError(String msg, int lineNumber) {
        super(msg, lineNumber, "");
    }
}
