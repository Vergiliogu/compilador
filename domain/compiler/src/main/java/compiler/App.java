package compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class App {

    private static final String FORMATTED_ERROR_MESSAGE = "Erro na linha %d - %s";
    private static final String PROGRAM_COMPILED_MESSAGE = "programa compilado com sucesso";
    private static final String FILE_COULD_NOT_BE_LOADED_MESSAGE = "arquivo não pode ser carregado: %s";

    public static void main(String[] args) {
        String filePath = args[0];
        File sourceCodeFile = new File(filePath);

        String output = PROGRAM_COMPILED_MESSAGE;

        try {
            List<String> lines = Files.readAllLines(sourceCodeFile.toPath());

            Semantic semantic = new Semantic();
            Lexical lexical = new Lexical(String.join("\n", lines));
            Syntactic syntactic = new Syntactic(lexical, semantic);

            syntactic.run();
        }
        catch (IOException e) { output = FILE_COULD_NOT_BE_LOADED_MESSAGE.formatted(sourceCodeFile.toPath()); }
        catch (CompilationError e) { output = FORMATTED_ERROR_MESSAGE.formatted(e.getLineNumber(), e.getErrorMessage()); }

        System.out.printf(output);
    }
}
