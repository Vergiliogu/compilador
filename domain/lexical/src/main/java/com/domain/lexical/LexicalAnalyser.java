package com.domain.lexical;

import java.util.LinkedList;
import java.util.List;

public class LexicalAnalyser {

    public LexicalResponse run(final String sourceCode) {
        try {
            Lexico lexico = new Lexico(sourceCode);

            Token t;

            List<Token> tokens = new LinkedList<>();

            while ( (t = lexico.nextToken()) != null )
                tokens.add(t);

            return LexicalResponse.success(Messages.PROGRAM_COMPILED, tokens);
        } catch (LexicalError e) {
            return LexicalResponse.error(formatAsErrorMessage(e));
        }
    }

    private String formatAsErrorMessage(LexicalError e) {
        return String.format(Messages.LEXICAL_ERROR, e.getLineNumber(), e.getMessage());
    }

    public static final class Messages {

        public static final String PROGRAM_COMPILED = "SUCCESS: Tokens created";
        public static final String LEXICAL_ERROR = "ERROR: linha %d: %s";
    }
}
