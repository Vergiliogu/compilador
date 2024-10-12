package compiler;

import java.util.Stack;

public class Syntactic {

    private final Lexical lexical;
    private final Semantic semantic;
    private final Stack<Integer> stack;

    private Token currentToken;
    private Token previousToken;

    public Syntactic(Lexical lexical, Semantic semantic) {
        this.lexical = lexical;
        this.semantic = semantic;
        stack = new Stack<>();
    }

    public void run() throws CompilationError {
        stack.clear();
        stack.push(Parser.DOLLAR);
        stack.push(Parser.START_SYMBOL);

        currentToken = lexical.nextToken();

        while (!step());
    }

    private boolean step() throws CompilationError {
        if (currentToken == null) {
            int pos = 0;
            if (previousToken != null)
                pos = previousToken.lineNumber() + previousToken.lexeme().length();

            currentToken = new Token(Parser.DOLLAR, "$", pos);
        }

        int x = stack.pop();
        int a = currentToken.tokenId();

        if (x == Parser.EPSILON)
            return false;

        if (isTerminal(x)) {
            if (x == a) {
                if (stack.empty())
                    return true;
                else {
                    previousToken = currentToken;
                    currentToken = lexical.nextToken();
                    return false;
                }
            }

            throwError(x, currentToken);
        }

        if (isNonTerminal(x)) {
            if (pushProduction(x, a))
                return false;

            throwError(x, currentToken);
        }

        semantic.executeAction(x-Parser.FIRST_SEMANTIC_ACTION, previousToken);

        return false;
    }

    private boolean isTerminal(int x) { return x < Parser.FIRST_NON_TERMINAL; }

    private boolean isNonTerminal(int x) { return x >= Parser.FIRST_NON_TERMINAL && x < Parser.FIRST_SEMANTIC_ACTION; }

    private void throwError(int x, Token token) throws CompilationError {
        String lexeme = token.tokenId() != Parser.STRING_CONSTANT ? token.lexeme() : "constante_string";
        String message = "encontrado %s %s".formatted(lexeme, Parser.PARSER_ERROR[x]);

        throw new CompilationError(message, currentToken.lineNumber());
    }

    private boolean pushProduction(int topStack, int tokenInput) {
        int p = Parser.PARSER_TABLE[topStack-Parser.FIRST_NON_TERMINAL][tokenInput-1];

        if (p >= 0) {
            int[] production = Parser.PRODUCTIONS[p];

            for (int i=production.length-1; i>=0; i--)
                stack.push(production[i]);

            return true;
        }

        return false;
    }
}
