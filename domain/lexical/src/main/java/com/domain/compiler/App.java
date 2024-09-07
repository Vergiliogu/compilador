package com.domain.compiler;

import com.domain.lexical.LexicalAnalyser;

public class App {

    public static void main(String[] args) {
        String sourceCode = "teste";

        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(sourceCode);

        lexicalAnalyser.run();
    }
}
