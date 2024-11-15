package compiler;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Runs the semantic analysis.
 */
public class Semantic {

    private static final String HEADER =
    """
    .assembly extern mscorlib {}
    .assembly _codigo_objeto{}
    .module _codigo_objeto.exe
    .class public UNICA{
    .method static public void _principal() {
    .entrypoint
    """;

    private static final String FOOTER =
    """
    ret
    }
    }
    """;

    private final Deque<Type> types = new ArrayDeque<>();
    private final StringBuilder out = new StringBuilder();

    public void executeAction(int action, Token token)	throws CompilationError {
        switch (action) {
            case 100: appendHeader();          break;
            case 101: appendFooter();          break;
            case 108: appendOutput();          break;
            case 123: appendSum();             break;
            case 128: appendExpression(token); break;
        }
    }

    private void appendSum() {
        compareTwoTypes();
        out.append("add").append("\n");
    }

    private void compareTwoTypes() {
        types.push(types.pop().take(types.pop()));
    }

    private void appendHeader() {
        out.append(HEADER);
    }

    private void appendFooter() {
        out.append(FOOTER);
    }

    private void appendOutput() {
        Type type = types.pop();

        if (type == Type.INT_64)
            out
                    .append("call void [mscorlib]System.Console::Write(%s)".formatted(Type.FLOAT_64.serialize()))
                    .append("\n");
    }

    private void appendExpression(final Token t) {
        switch (t.tokenId()) {
            case Token.INTEGER: handleInteger(t.lexeme()); break;
        }
    }

    private void handleInteger(String lexeme) {
        types.push(Type.INT_64);
        out.append("ldc.i8 ").append(lexeme).append("\n");
        out.append("conv.r8").append("\n");
    }

    /**
     * Once the semantic analysis ran, the object code will output from here.
     */
    public String getObjectCode() {
        return out.toString();
    }

    private enum Type {

        INT_64("int64"),
        FLOAT_64("float64"),
        BOOLEAN("bool"),
        STRING("string");

        private final String type;

        Type(final String type) {
            this.type = type;
        }

        public String serialize() {
            return type;
        }

        /**
         * Compare this type to another type.
         */
        public Type take(Type anotherType) {
            if (this == INT_64 && anotherType == INT_64) return INT_64;
            return FLOAT_64;
        }
    }
}