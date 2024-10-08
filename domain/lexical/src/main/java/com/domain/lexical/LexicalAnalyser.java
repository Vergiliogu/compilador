package com.domain.lexical;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LexicalAnalyser {

    public static final String PROGRAM_COMPILED_MESSAGE = "SUCCESS: Tokens created";
    public static final String LEXICAL_ERROR_MESSAGE = "ERROR: linha %d: %s";

    private final String sourceCode;

    private int currentCharPosition;

    private int lineNumber = 1;
    private int blockCommentStartLineNumber = 0;

    public LexicalAnalyser(String sourceCode) {
        this.sourceCode = sourceCode;
        this.currentCharPosition = 0;
    }

    public LexicalResponse run() {
        try {
            LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(sourceCode);

            Token t;

            List<Token> tokens = new LinkedList<>();

            while ( (t = lexicalAnalyser.nextToken()) != null )
                tokens.add(t);

            return LexicalResponse.success(LexicalAnalyser.PROGRAM_COMPILED_MESSAGE, tokens);
        } catch (LexicalError e) {
            return LexicalResponse.error(String.format(LexicalAnalyser.LEXICAL_ERROR_MESSAGE, e.getLineNumber(), e.getMessage()));
        }
    }

    private Token nextToken() throws LexicalError {
        if (!hasInput()) return null;

        int start = currentCharPosition;
        char readChar;
        int currentState = 0;
        int previousState = 0;
        int finalState = -1;
        int end = -1;

        while (hasInput()) {
            previousState = currentState;
            readChar = nextChar();
            currentState = nextState(readChar, currentState);

            if (currentState < 0) break;

            if (readChar == '\n') lineNumber++;

            checkForBlockComment(readChar);

            boolean isFinalState = tokenForState(currentState) >= 0;

            if (isFinalState) {
                finalState = currentState;
                end = currentCharPosition;
            }
        }

        validateFinalState(finalState, currentState, previousState, start);

        return buildToken(finalState, start, end);
    }

    private void checkForBlockComment(char currChar) {
        boolean hasMoreChars = currentCharPosition < sourceCode.length();
        boolean isBlockCommentStartSeq = currChar == '>' && sourceCode.charAt(currentCharPosition) == '@';

        if (hasMoreChars && isBlockCommentStartSeq)
                blockCommentStartLineNumber = lineNumber;
    }

    private void validateFinalState(int finalState, int currentState, int previousState, int start) throws LexicalError {
        if (finalState < 0 || (finalState != currentState && tokenForState(previousState) == -2))
            throwErrorForState(previousState, start);
    }

    private void throwErrorForState(int state, int start) throws LexicalError {
        String error = Scanner.SCANNER_ERROR[state];

        switch (error) {
            case Scanner.INVALID_STRING ->
                    throw new LexicalError(error, lineNumber);
            case Scanner.INVALID_BLOCK_COMENT ->
                    throw new LexicalError(error, blockCommentStartLineNumber);
            case Scanner.INVALID_SYMBOL ->
                    throw new LexicalError(String.format("%s %s", sourceCode.charAt(currentCharPosition - 1), error), lineNumber);
            case Scanner.INVALID_IDENTIFIER ->
                    throw new LexicalError(String.format("%s %s", sourceCode.substring(start, currentCharPosition), error), lineNumber);
        }
    }

    private Token buildToken(int finalState, int start, int end) throws LexicalError {
        currentCharPosition = end;

        int tokenForFinalState = tokenForState(finalState);

        if (tokenForFinalState == 0)
            return nextToken();

        String lexeme = sourceCode.substring(start, end);
        int token = lookupToken(tokenForFinalState, lexeme);

        validateReservedWord(token, lexeme);

        return new Token(token, lexeme, lineNumber);
    }

    private void validateReservedWord(int token, String lexeme) throws LexicalError {
        boolean isReservedWord = Word.RESERVED_WORD.getId() == token;
        boolean invalidReservedWord = Arrays.stream(Scanner.SPECIAL_CASES_KEYS).noneMatch(e -> e.equals(lexeme));

        if (isReservedWord && invalidReservedWord)
            throw new LexicalError(String.format("%s %s", lexeme, Scanner.INVALID_RESERVED_WORD), lineNumber);
    }

    private int nextState(char c, int state) {
        int start = Scanner.SCANNER_TABLE_INDEXES[state];
        int end = Scanner.SCANNER_TABLE_INDEXES[state + 1] - 1;

        while (start <= end) {
            int half = (start + end) / 2;
            if (Scanner.SCANNER_TABLE[half][0] == c) {
                return Scanner.SCANNER_TABLE[half][1];
            } else if (Scanner.SCANNER_TABLE[half][0] < c) {
                start = half + 1;
            } else {
                end = half - 1;
            }
        }

        return -1;
    }

    private int tokenForState(int state) {
        if (state < 0 || state >= Scanner.TOKEN_STATE.length)
            return -1;

        return Scanner.TOKEN_STATE[state];
    }

    public int lookupToken(int base, String key) {
        int start = Scanner.SPECIAL_CASES_INDEXES[base];
        int end = Scanner.SPECIAL_CASES_INDEXES[base + 1] - 1;

        while (start <= end) {
            int half = (start + end) / 2;
            int comp = Scanner.SPECIAL_CASES_KEYS[half].compareTo(key);

            if (comp == 0) {
                return Scanner.SPECIAL_CASES_VALUES[half];
            } else if (comp < 0) {
                start = half + 1;
            } else {
                end = half - 1;
            }
        }

        return base;
    }

    private char nextChar() {
        if (hasInput()) return sourceCode.charAt(currentCharPosition++);

        return (char) -1;
    }

    private boolean hasInput() {
        return currentCharPosition < sourceCode.length();
    }
}
