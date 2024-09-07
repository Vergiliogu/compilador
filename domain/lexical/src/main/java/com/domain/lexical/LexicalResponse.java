package com.domain.lexical;

import java.util.LinkedList;
import java.util.List;

public class LexicalResponse {

    private final boolean success;
    private final String message;
    private final List<Token> tokens;

    public static LexicalResponse success(String message, List<Token> tokens) {
        return new LexicalResponse(true, message, tokens);
    }

    public static LexicalResponse error(String errorMessage) {
        return new LexicalResponse(true, errorMessage, new LinkedList<>());
    }

    private LexicalResponse(boolean success, String message, List<Token> tokens) {
        this.success = success;
        this.message = message;
        this.tokens = tokens;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
