package com.domain.lexical;

import java.util.Arrays;

public class Lexico {

    private final String sourceCode;

    private int currentCharPosition;

    private int lineNumber = 1;
    private int blockCommentStartLineNumber = 0;

    public Lexico(String sourceCode) {
        this.sourceCode = sourceCode;
        this.currentCharPosition = 0;
    }

    public Token nextToken() throws LexicalError {
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
        String error = ScannerConstants.SCANNER_ERROR[state];

        switch (error) {
            case ScannerConstants.INVALID_STRING ->
                    throw new LexicalError(error, lineNumber);
            case ScannerConstants.INVALID_BLOCK_COMENT ->
                    throw new LexicalError(error, blockCommentStartLineNumber);
            case ScannerConstants.INVALID_SYMBOL ->
                    throw new LexicalError(String.format("%s %s", sourceCode.charAt(currentCharPosition - 1), error), lineNumber);
            case ScannerConstants.INVALID_IDENTIFIER ->
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

        return new Token(token, lexeme, start);
    }

    private void validateReservedWord(int token, String lexeme) throws LexicalError {
        boolean isReservedWord = Words.RESERVED_WORD.get() == token;
        boolean invalidReservedWord = Arrays.stream(ScannerConstants.SPECIAL_CASES_KEYS).noneMatch(e -> e.equals(lexeme));

        if (isReservedWord && invalidReservedWord)
            throw new LexicalError(String.format("%s %s", lexeme, ScannerConstants.INVALID_RESERVED_WORD), lineNumber);
    }

    private int nextState(char c, int state) {
        int start = ScannerConstants.SCANNER_TABLE_INDEXES[state];
        int end = ScannerConstants.SCANNER_TABLE_INDEXES[state + 1] - 1;

        while (start <= end) {
            int half = (start + end) / 2;
            if (ScannerConstants.SCANNER_TABLE[half][0] == c) {
                return ScannerConstants.SCANNER_TABLE[half][1];
            } else if (ScannerConstants.SCANNER_TABLE[half][0] < c) {
                start = half + 1;
            } else {
                end = half - 1;
            }
        }

        return -1;
    }

    private int tokenForState(int state) {
        if (state < 0 || state >= ScannerConstants.TOKEN_STATE.length)
            return -1;

        return ScannerConstants.TOKEN_STATE[state];
    }

    public int lookupToken(int base, String key) {
        int start = ScannerConstants.SPECIAL_CASES_INDEXES[base];
        int end = ScannerConstants.SPECIAL_CASES_INDEXES[base + 1] - 1;

        while (start <= end) {
            int half = (start + end) / 2;
            int comp = ScannerConstants.SPECIAL_CASES_KEYS[half].compareTo(key);

            if (comp == 0) {
                return ScannerConstants.SPECIAL_CASES_VALUES[half];
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
