package com.domain.lexical;

public class Token {

    private final int id;
    private final String lexeme;
    private final int lineNumber;

    public Token(int id, String lexeme, int lineNumber) {
        this.id = id;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    public final int getId() {
        return id;
    }

    public final String getLexeme() {
        return lexeme;
    }

    public final int getLineNumber() {
        return lineNumber;
    }
}
