package com.domain.compiler;

import com.domain.lexical.LexicalAnalyser;

public class Compiler {

    public static void main(String[] args) {
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser();

        String sourceCode = "teste";

        lexicalAnalyser.run(sourceCode);
    }
}
