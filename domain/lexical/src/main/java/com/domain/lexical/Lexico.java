package com.domain.lexical;

public class Lexico implements Constants {

    private int position;
    private String input;

    public Lexico(String input) {
        setInput(input);
    }

    public void setInput(String input) {
        this.input = input;
        setPosition(0);
    }

    public void setPosition(int pos) {
        position = pos;
    }

    public Token nextToken() throws LexicalError {
        if (!hasInput())
            return null;

        int start = position;

        int state = 0;
        int lastState = 0;
        int endState = -1;
        int end = -1;

        while (hasInput()) {
            lastState = state;
            
            state = nextState(nextChar(), state);

            if (state < 0)
                break;

            if (tokenForState(state) >= 0) {
                endState = state;
                end = position;
            }
        }
        if (endState < 0 || (endState != state && tokenForState(lastState) == -2))
            throw new LexicalError(ScannerConstants.SCANNER_ERROR[lastState], start, " | " + start + " " + end + " | "); // TODO: pass lexeme

        position = end;

        int token = tokenForState(endState);

        if (token == 0)
            return nextToken();

        String lexeme = input.substring(start, end);
        token = lookupToken(token, lexeme);

        if (token == Constants.t_palavra_reservada)
            throw new LexicalError(ScannerConstants.SCANNER_ERROR[lastState], start, lexeme);

        return new Token(token, lexeme, start);
    }

    private int nextState(char c, int state) {
        int start = ScannerConstants.SCANNER_TABLE_INDEXES[state];
        int end   = ScannerConstants.SCANNER_TABLE_INDEXES[state + 1] - 1;

        while (start <= end) {
            int half = (start+end)/2;

            if (ScannerConstants.SCANNER_TABLE[half][0] == c)
                return ScannerConstants.SCANNER_TABLE[half][1];
            else if (ScannerConstants.SCANNER_TABLE[half][0] < c)
                start = half+1;
            else  //(SCANNER_TABLE[half][0] > c)
                end = half-1;
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

            if (comp == 0)
                return ScannerConstants.SPECIAL_CASES_VALUES[half];
            else if (comp < 0)
                start = half+1;
            else  // (comp > 0)
                end = half-1;
        }

        return base;
    }

    private boolean hasInput() {
        return position < input.length();
    }

    private char nextChar() {
        if (hasInput())
            return input.charAt(position++);

        return (char) -1;
    }
}
