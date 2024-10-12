package compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class App {

    private static final String FORMATED_ERROR = "erro linha %d - %s";
    private static final String PROGRAM_COMPILED = "programa compilado com sucesso";
    private static final String FILE_COULD_NOT_BE_LOADED = "arquivo não pode ser carregado: %s";

    public static void main(String[] args) {
        String output = PROGRAM_COMPILED;

        String filePath = args[0];
        File sourceCodeFile = new File(filePath);

        try {
            List<String> lines = Files.readAllLines(sourceCodeFile.toPath());

            Semantic semantic = new Semantic();
            Lexical lexical = new Lexical(String.join("\n", lines));
            Syntactic syntactic = new Syntactic(lexical, semantic);

            syntactic.run();
        }
        catch (IOException e) { output = FILE_COULD_NOT_BE_LOADED.formatted(sourceCodeFile.toPath()); }
        catch (CompilationError e) { output = FORMATED_ERROR.formatted(e.getLineNumber(), e.getErrorMessage()); }

        System.out.printf(output);
    }
}