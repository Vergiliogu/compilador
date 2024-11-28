package compiler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Runs the semantic analysis.
 */
public class Semantic {

    private static final String HEADER = """
            .assembly extern mscorlib {}
            .assembly _codigo_objeto{}
            .module _codigo_objeto.exe
            .class public UNICA{
            .method static public void _principal() {
            .entrypoint
            """;

    private static final String FOOTER = """
            ret
            }
            }
            """;

    private final Deque<Type> types = new ArrayDeque<>();
    private final List<String> ids = new LinkedList<>();
    private final List<String> symbols = new LinkedList<>();
    private final Deque<String> labels = new ArrayDeque<>();
    private String relationalOperator = "";

    private final StringBuilder out = new StringBuilder();

    public void executeAction(int action, Token t) throws CompilationError {
        switch (action) {
            case 100:
                appendHeader();
                break;
            case 101:
                appendFooter();
                break;
            case 102:
                appendId(t);
                break;
            case 103:
                appendAttribution(t);
                break;
            case 104:
                ids.add(t.lexeme());
                break;
            case 105:
                appendInput(t);
                break;
            case 106:
                appendOutString(t.lexeme());
                break;
            case 107:
                appendLineBreak();
                break;
            case 108:
                appendOutput();
                break;
            case 109:
                n109();
                break;
            case 110:
                n110();
                break;
            case 111:
                n111();
                break;
            case 112:
                n112();
                break;
            case 116:
                appendAnd();
                break;
            case 117:
                appendOr();
                break;
            case 118:
                appendTrue();
                break;
            case 119:
                appendFalse();
                break;
            case 120:
                appendNot();
                break;
            case 121:
                appendRelationalOperator(t);
                break;
            case 122:
                appendLogicalOperation();
                break;
            case 123:
                appendSum();
                break;
            case 124:
                appendSub();
                break;
            case 125:
                appendMul();
                break;
            case 126:
                appendDiv();
                break;
            case 127:
                n127(t);
                break;
            case 128:
                appendInt(t.lexeme());
                break;
            case 129:
                appendFloat(t.lexeme());
                break;
            case 130:
                appendString(t.lexeme());
                break;
            case 131:
                appendMinus();
                break;
        }
    }

    // 100
    private void appendHeader() {
        out.append(HEADER);
    }

    // 101
    private void appendFooter() {
        out.append(FOOTER);
    }

    // 102
    private void appendId(Token t) throws CompilationError {
        for (String id : ids) {
            if (symbols.contains(id)) {
                throw new CompilationError("%s já declarado".formatted(id), t.lineNumber());
            }

            symbols.add(id);

            Type type = retrieveTypeFromId(id);

            out.append(".locals (%s %s)".formatted(type.serialize(), id)).append("\n");
        }

        ids.clear();
    }

    // 103
    private void appendAttribution(Token t) throws CompilationError {
        if (types.pop() == Type.INT_64) {
            out.append("conv.r8").append("\n");
        }

        for (int i = 0; i < ids.size() - 1; i++) {
            out.append("dup").append("\n");
        }

        for (String id : ids) {
            if (!symbols.contains(id)) {
                throw new CompilationError("%s não declarado".formatted(id), t.lineNumber());
            }

            out.append("stdloc %s".formatted(id)).append("\n");
        }

        ids.clear();
    }

    // 105
    private void appendInput(Token t) throws CompilationError {
        String id = t.lexeme();
        Type idType = retrieveTypeFromId(id);

        if (!symbols.contains(id)) {
            throw new CompilationError("%s não declarado".formatted(id), t.lineNumber());
        }

        out.append("call string [mscorlib]System.Console::ReadLine()".formatted(id)).append("\n");
        out.append("call %s [mscorlib]System.<classe>::Parse(string)".formatted(idType)).append("\n");
        out.append("stdloc %s".formatted(id)).append("\n");
    }

    // 106
    private void appendOutString(String lexeme) {
        appendString(lexeme);
        types.push(Type.STRING);
        appendOutput();
    }

    // 107
    private void appendLineBreak() {
        out.append("ldstr \"\\n\"").append("\n");
        types.push(Type.STRING);
        appendOutput();
    }

    // 108
    private void appendOutput() {
        Type type = types.pop();

        String outputLine = "call void [mscorlib]System.Console::Write(%s)";

        if (type == Type.INT_64)
            out.append(outputLine.formatted(Type.FLOAT_64.serialize()));
        else
            out.append(outputLine.formatted(type.serialize()));

        out.append("\n");
    }

    // 109
    private void n109() {
        String label1 = "L" + labels.size();
        labels.push(label1);
        String label2 = "L" + labels.size();
        labels.push(label2);

        out.append("brfalse %s".formatted(label2)).append("\n");
    }

    // 110
    private void n110() {
        String label2 = labels.pop();
        String label1 = labels.pop();

        out.append("br %s".formatted(label1)).append("\n");

        labels.push(label1);

        out.append("%s:".formatted(label2)).append("\n");
    }

    // 111
    private void n111() {
        String label = labels.pop();

        out.append("%s:".formatted(label)).append("\n");
    }

    // 112
    private void n112() {
        String label = "L" + labels.size();

        out.append("brfalse %s".formatted(label)).append("\n");

        labels.push(label);
    }

    // 116
    private void appendAnd() {
        types.pop();
        types.pop();

        out.append("and").append("\n");

        types.add(Type.BOOLEAN);
    }

    // 117
    private void appendOr() {
        types.pop();
        types.pop();

        out.append("or").append("\n");

        types.add(Type.BOOLEAN);
    }

    // 118
    private void appendTrue() {
        types.add(Type.BOOLEAN);
        out.append("ldc.i4.1").append("\n");
    }

    // 119
    private void appendFalse() {
        types.add(Type.BOOLEAN);
        out.append("ldc.i4.0").append("\n");
    }

    // 120
    private void appendNot() {
        out.append("ldc.i4.1").append("\n");
        out.append("xor").append("\n");
    }

    // 121
    private void appendRelationalOperator(Token t) {
        relationalOperator = t.lexeme();
    }

    // 122
    private void appendLogicalOperation() throws IllegalStateException {
        types.pop();
        types.pop();

        switch (relationalOperator) {
            case "==":
                out.append("ceq").append("\n");
                break;
            case "!=":
                out.append("ceq").append("\n");
                out.append("ldc.i4.1").append("\n");
                out.append("xor").append("\n");
                break;
            case "<":
                out.append("clt").append("\n");
                break;
            case ">":
                out.append("cgt").append("\n");
                break;
            default:
                throw new IllegalStateException("Operador relacional inválido: " + relationalOperator);
        }

        types.push(Type.BOOLEAN);
    }

    // 123
    private void appendSum() {
        compareTwoTypesAndPushResultingType();
        out.append("add").append("\n");
    }

    // 124
    private void appendSub() {
        compareTwoTypesAndPushResultingType();
        appendMinus();
    }

    // 125
    private void appendMul() {
        compareTwoTypesAndPushResultingType();
        out.append("mul").append("\n");
    }

    // 126
    private void appendDiv() {
        compareTwoTypesAndPushResultingType();
        out.append("div").append("\n");
    }

    // 127
    private void n127(Token t) throws CompilationError {
        String id = t.lexeme();
        if (!symbols.contains(id)) {
            throw new CompilationError("%s não declarado".formatted(id), t.lineNumber());
        }

        Type type = retrieveTypeFromId(id);
        types.push(type);
        out.append("ldloc %s".formatted(id)).append("\n");

        if (type == Type.INT_64) {
            out.append("conv.r8").append("\n");
        }
    }

    // 128
    private void appendInt(String lexeme) {
        types.push(Type.INT_64);
        out.append("ldc.i8 %s".formatted(lexeme)).append("\n");
        out.append("conv.r8").append("\n");
    }

    // 129
    private void appendFloat(String lexeme) {
        types.push(Type.FLOAT_64);
        out.append("ldc.r8 %s".formatted(lexeme.replaceAll(",", "."))).append("\n");
    }

    // 130
    private void appendString(String lexeme) {
        types.push(Type.STRING);
        out.append("ldstr %s".formatted(lexeme)).append("\n");
    }

    // 131
    private void appendMinus() {
        out.append("sub").append("\n");
    }

    /**
     * Once the semantic analysis ran, the object code will output from here.
     */
    public String getObjectCode() {
        return out.toString();
    }

    // utilities

    private void compareTwoTypesAndPushResultingType() {
        types.push(types.pop().take(types.pop()));
    }

    private Type retrieveTypeFromId(String id) {
        return switch (id.charAt(0)) {
            case 'i' -> Type.INT_64;
            case 'f' -> Type.FLOAT_64;
            case 's' -> Type.STRING;
            case 'b' -> Type.BOOLEAN;
            default -> throw new IllegalStateException("Unexpected value: " + id.charAt(0));
        };
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
         * Compare that type to <code>anotherType</code>.
         */
        public Type take(Type anotherType) {
            if (this == INT_64 && anotherType == INT_64)
                return INT_64;
            return FLOAT_64;
        }
    }
}