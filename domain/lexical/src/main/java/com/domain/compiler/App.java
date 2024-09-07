package com.domain.compiler;

import com.domain.lexical.LexicalAnalyser;
import com.domain.lexical.LexicalResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class App {

    public static void main(String[] args) {
        String filePath = args[0];

        File sourceCodeFile = new File(filePath);

        try {
            List<String> lines = Files.readAllLines(sourceCodeFile.toPath());

            LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(String.join("\n", lines));

            LexicalResponse response = lexicalAnalyser.run();

            System.out.println(response);
        } catch (IOException e) {
            System.err.println("File not found for path: " + filePath);
        }
    }
}
