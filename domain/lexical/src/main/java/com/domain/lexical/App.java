package com.domain.lexical;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class App {

    public static void main(String[] args) {
        String filePath = args[0];

        File file = new File(filePath);

        try {
            String fileContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            Lexico lexico = new Lexico(fileContent);
        
            Token t;

            while ( (t = lexico.nextToken()) != null ) {
                System.out.println(t.getId());
                System.out.println(t.getLexeme());
            }
            
        }
        catch ( LexicalError e ) {
            System.err.println(e.getMessage() + " : " + e.getLexeme() + " - " + e.getPosition());
        }
        catch ( IOException e ) {
            System.err.println("File not found for path " + filePath);
        }
    }
}
