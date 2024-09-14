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
        return new LexicalResponse(false, errorMessage, new LinkedList<>());
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

    @Override
    public String toString() {
        String response =
                """
                {
                    "success": %b,
                    "message": "%s",
                    "tokens": [ %s ]
                }
                """;

        return response.formatted(success, message, tokensAsJsonList());

    }

    private String tokensAsJsonList() {
        StringBuilder sb = new StringBuilder();

        if (tokens.isEmpty()) return "";

        Token lastToken = tokens.remove(tokens.size() - 1);

        for (Token t : tokens)
            sb.append(String.format("%s, ", t));

        sb.append(String.format("%s", lastToken));

        return sb.toString();
    }
}
