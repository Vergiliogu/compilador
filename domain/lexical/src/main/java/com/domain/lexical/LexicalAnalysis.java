package com.domain.lexical;

public class LexicalAnalysis {

    public String run(final String sourceCode) {
        try {
            Lexico lexico = new Lexico(sourceCode);

            Token t;

            StringBuilder tokens = new StringBuilder();

            while ( (t = lexico.nextToken()) != null ) {
                tokens.append(t.getId()).append(";");
                tokens.append(t.getLexeme()).append(";");
                tokens.append(t.getPosition()).append(";");
                tokens.append("\n");
            }

            // Store tokens at a file and pass SUCCESS: Tokens at: file

            return Messages.PROGRAM_COMPILED;
        } catch (LexicalError e) {
            return Messages.LEXICAL_ERROR + formatError(e);
        }
    }

    private String formatError(LexicalError e) {
        return String.format("linha %d: %s", e.getPosition(), e.getMessage());
    }

    public static final class Messages {

        private static final String SUCCESS = "SUCCESS: ";
        private static final String ERROR = "ERROR: ";

        public static final String PROGRAM_COMPILED = SUCCESS;
        public static final String LEXICAL_ERROR = ERROR;
    }
}
