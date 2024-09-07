package com.domain.compiler;

import com.domain.lexical.LexicalAnalysis;

public class Compiler {

    public static void main(String[] args) {
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();

        String sourceCode = "teste";

        lexicalAnalysis.run(sourceCode);
    }
}
