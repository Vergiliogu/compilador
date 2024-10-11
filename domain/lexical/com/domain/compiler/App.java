package com.domain.compiler;

import com.domain.lexical.Lexical;
import com.domain.lexical.Syntatic;
import com.domain.lexical.CompilationError;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class App {
	
	private static final String FILE_NOT_FOUND = "Arquivo não encontrado: %s";
	private static final String PROGRAM_COMPILED = "Programa compilado com sucesso";
	private static final String COMPILING_ERROR = "Erro na linha %d - %s";

    public static void main(String[] args) {
//        String filePath = args[0];
//
//        File sourceCodeFile = new File(filePath);
    	
    	String output = PROGRAM_COMPILED;
        
        try {
//            List<String> lines = Files.readAllLines(sourceCodeFile.toPath());

            String sourceCode = String.join("\n", "main b_bbb= true;write(end");
            
            Lexical lexical = new Lexical(sourceCode);
            
            new Syntatic(lexical).parse();
        } 
//        catch (IOException e) { output = FILE_NOT_FOUND.formatted(filePath); }
        catch (CompilationError e) { output = COMPILING_ERROR.formatted(e.getLineNumber(), e.getMessage()); }
        
        System.out.println(output);
    }
}
