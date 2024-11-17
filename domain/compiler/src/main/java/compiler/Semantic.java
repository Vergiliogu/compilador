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

    public void executeAction(int action, Token t)throws CompilationError {
        switch (action) {
            case 100: appendHeader();          break;
            case 101: appendFooter();          break;
            case 107: appendLineBreak();       break;
            case 108: appendOutput();          break;
            case 123: appendSum();             break;
            case 128: appendInt(t.lexeme());   break;
            case 129: appendFloat(t.lexeme()); break;
        }
    }

    private void appendSum() {
        compareTwoTypesAndPushResulting();
        out.append("add").append("\n");
    }

    private void compareTwoTypesAndPushResulting() {
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

        String outputLine = "call void [mscorlib]System.Console::Write(%s)";

        if (type == Type.INT_64)
            out.append(outputLine.formatted(Type.FLOAT_64.serialize()));
        else
            out.append(outputLine.formatted(type.serialize()));

        out.append("\n");
    }

    private void appendLineBreak() {
        out.append("ldstr \"\\n\"").append("\n");
        types.push(Type.STRING);
        appendOutput();
    }

    private void appendInt(String lexeme) {
        types.push(Type.INT_64);
        out.append("ldc.i8 ").append(lexeme).append("\n");
        out.append("conv.r8").append("\n");
    }

    private void appendFloat(String lexeme) {
        types.push(Type.FLOAT_64);
        out.append("ldc.r8 ").append(lexeme.replaceAll(",", ".")).append("\n");
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