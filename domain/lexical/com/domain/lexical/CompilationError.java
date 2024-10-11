package com.domain.lexical;

public class CompilationError extends Exception {

	private static final long serialVersionUID = 1624135260566386995L;
		
	private final int lineNumber;

    public CompilationError(String message, int lineNumber) {
        super(message);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
