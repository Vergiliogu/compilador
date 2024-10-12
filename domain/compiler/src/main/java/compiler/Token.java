package compiler;

/**
 * Represents a token generated from lexical analysis.
 *
 * <p>Takes an id, the lexeme and the line number.
 */
public record Token(int tokenId, String lexeme, int lineNumber) { }
