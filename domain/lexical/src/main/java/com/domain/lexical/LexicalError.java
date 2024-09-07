package com.domain.lexical;

public class LexicalError extends Exception {

    private final int lineNumber;

    public LexicalError(String msg, int lineNumber) {
        super(msg);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
