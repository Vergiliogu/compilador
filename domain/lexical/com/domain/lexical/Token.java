package com.domain.lexical;

public class Token {

    private final Word word;
    private final String lexeme;
    private final int lineNumber;

    public Token(int id, String lexeme, int lineNumber) {
        this.word = Word.fromId(id);
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    public final Word getWord() {
        return word;
    }

    public final String getLexeme() {
        return lexeme;
    }

    public final int getLineNumber() {
        return lineNumber;
    }
}
