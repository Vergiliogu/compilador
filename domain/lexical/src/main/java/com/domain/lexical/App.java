package com.domain.lexical;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class App {

    public static void main(String[] args) {
        String filePath = "D:/dev/compilador/domain/test.txt";

        File file = new File(filePath);

        try {
            List<String> fileContent = Files.readAllLines(file.toPath());

            String joinedLineFeed = String.join("\n", fileContent);

            Lexico lexico = new Lexico(joinedLineFeed);
        
            Token t;

            while ( (t = lexico.nextToken()) != null ) {
                System.out.println(t.getId());
                System.out.println(t.getLexeme());
            }
            
        } catch (LexicalError e) {
            System.err.println(e.getMessage() + " : " + e.getLexeme() + " - " + e.getPosition());
        } catch (IOException e) {
            System.err.println("File not found for path: " + filePath);
        }
    }
}
