package compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class App {

    private static final String FORMATTED_ERROR_MESSAGE = "Erro na linha %d - %s";
    private static final String PROGRAM_COMPILED_MESSAGE = "programa compilado com sucesso";
    private static final String FILE_COULD_NOT_BE_LOADED_MESSAGE = "arquivo não pode ser carregado: %s";
    private static final String FILE_COULD_NOT_BE_CREATED_MESSAGE = "arquivo ilasm %s não pode ser escrito";

    public static void main(String[] args) {
        String filePath = args[0];
        File sourceCodeFile = new File(filePath);

        Semantic semantic = new Semantic();

        try {
            List<String> lines = Files.readAllLines(sourceCodeFile.toPath());

            Lexical lexical = new Lexical(String.join("\n", lines));
            Syntactic syntactic = new Syntactic(lexical, semantic);

            syntactic.run();
        }

        catch (IOException e) {
            System.out.printf(FILE_COULD_NOT_BE_LOADED_MESSAGE.formatted(sourceCodeFile.toPath()));
            return;
        }

        catch (CompilationError e) {
            System.out.printf(FORMATTED_ERROR_MESSAGE.formatted(e.getLineNumber(), e.getErrorMessage()));
            return;
        }

        writeIlasmFile(sourceCodeFile, semantic.getObjectCode());

        System.out.printf(PROGRAM_COMPILED_MESSAGE);
    }

    private static void writeIlasmFile(final File sourceCodeFile, final String objectCodeContent) {
        String fileName = sourceCodeFile.getAbsolutePath();

        File ilasmFile = new File("%s.il".formatted(fileName));

        try (FileOutputStream fileInputStream = new FileOutputStream(ilasmFile)) {
            fileInputStream.write(objectCodeContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println(FILE_COULD_NOT_BE_CREATED_MESSAGE.formatted(fileName));
        }
    }
}
