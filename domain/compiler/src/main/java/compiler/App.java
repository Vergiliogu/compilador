package compiler;

public class App {

    private static final String FORMATED_ERROR = "erro linha %d - %s";
    private static final String PROGRAM_COMPILED = "programa compilado com sucesso";

    public static void main(String[] args) {
        // TODO: run from file

        String sourceCode = """
                main
                b_aa = false;
                b_bb = true && b_aa;
                write("something", b_bb);
                end
                """;

        String ouput = PROGRAM_COMPILED;

        Semantic semantic = new Semantic();
        Lexical lexical = new Lexical(sourceCode);
        Syntactic syntactic = new Syntactic(lexical, semantic);

        try { syntactic.run(); }
        catch (compiler.CompilationError e) { ouput = FORMATED_ERROR.formatted(e.getLineNumber(), e.getErrorMessage()); }

        System.out.printf(ouput);
    }
}
