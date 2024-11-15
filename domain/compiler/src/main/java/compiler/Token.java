package compiler;

/**
 * Represents a token generated from lexical analysis.
 *
 * <p>Takes an id, the lexeme and the line number.
 *
 * @see Scanner Scanner - for more context on token id.
 */
public record Token(int tokenId, String lexeme, int lineNumber) {
    public static final int INTEGER = 17;
}
