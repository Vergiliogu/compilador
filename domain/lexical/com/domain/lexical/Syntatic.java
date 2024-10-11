package com.domain.lexical;

import java.util.Stack;

public class Syntatic {
	
    private Stack<Integer> stack;
    
    private Lexical lexicalAnalyser;
    
    private Token currentToken;
    private Token previousToken;
    
    public Syntatic(Lexical lexicalAnalyser) {
    	this.lexicalAnalyser = lexicalAnalyser;
    	this.stack = new Stack<>();
    }

    public void parse() throws CompilationError {
        stack.push(Word.DOLLAR.getId());
        stack.push(Parser.START_SYMBOL);

        currentToken = lexicalAnalyser.nextToken();

        while (!step());
    }

    private boolean step() throws CompilationError {
        if (currentToken == null) {
            int pos = 0;
            
            if (previousToken != null)
                pos = previousToken.getLineNumber() + previousToken.getLexeme().length();

            currentToken = new Token(Word.DOLLAR.getId(), Word.DOLLAR.getWord(), pos);
        }

        int x = stack.pop();
        int a = currentToken.getWord().getId();

        if (x == Word.EPSILON.getId())
            return false;
        
        else if (isTerminal(x)) {
            if (x == a) {
                if (stack.empty())
                    return true;
                else {
                    previousToken = currentToken;
                    currentToken = lexicalAnalyser.nextToken();
                    return false;
                }
            }
            else throw syntaticError(x, currentToken);
        }
        
        else if (isNonTerminal(x))
            if (pushProduction(x, a)) return false;
            else throw syntaticError(x, currentToken);
        
        
        return false; // TODO: semantic analysis goes here
    }
    
    private CompilationError syntaticError(int x, Token token) {
    	String lexeme = token.getWord().equals(Word.STRING) ? "constante_string" : token.getLexeme();
    	String message = "encontrado %s %s".formatted(lexeme, Parser.PARSER_ERROR[x]);
    	
    	return new CompilationError(message, currentToken.getLineNumber());
    }

    private boolean pushProduction(int topStack, int tokenInput) {
        int p = Parser.PARSER_TABLE[topStack - Parser.FIRST_NON_TERMINAL][tokenInput - 1];
        
        if (p < 0) return false;
        
        int[] production = Parser.PRODUCTIONS[p];

        for (int i = production.length-1; i >= 0; i--)
        	stack.push(production[i]);
            
        return true;
    }
    
    private static final boolean isTerminal(int x) { return x < Parser.FIRST_NON_TERMINAL; }

    private static final boolean isNonTerminal(int x) { return x >= Parser.FIRST_NON_TERMINAL && x < Parser.FIRST_SEMANTIC_ACTION; }
    
    interface Parser {

    	int START_SYMBOL = 36;

        int FIRST_NON_TERMINAL    = 36;
        int FIRST_SEMANTIC_ACTION = 73;

        int[][] PARSER_TABLE = {
            { -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1,  1, -1, -1, -1, -1,  1,  1,  1,  1, -1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1,  3,  2, -1, -1, -1, -1,  2,  2,  2,  2, -1, -1,  2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1,  7, -1, -1, -1, -1,  5,  6,  6,  8, -1, -1,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10,  9, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 72, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 73, 74, 74, -1, -1 },
            { -1, -1, -1, -1, 14, -1, -1, -1, -1, 12, 13, 13, 15, -1, -1, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, 24, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, 32, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 18, -1, -1, 18, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 19, -1, -1, -1, 20 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 22, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, 69, 69, -1, -1, -1, -1, -1, -1, 69, 69, 69, 69, -1, -1, 69, -1, -1, -1, -1, 69, 69, -1, -1, -1, -1, -1, 69, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 70, -1, -1, -1, 71 },
            { -1, -1, -1, -1, 66, -1, -1, -1, -1, 66, 66, 66, 66, -1, -1, 66, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, 68, 67, 68, 68, -1, -1, 67, 67, 67, 67, 68, 68, 67, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, 26, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, 27, -1, 28, 27, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, 30, -1, -1, 29, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 34, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, 36, 36, -1, -1, -1, -1, -1, -1, 36, 36, 36, 36, -1, -1, 36, -1, -1, -1, -1, 36, 36, -1, -1, -1, -1, -1, 36, -1 },
            { -1, -1, -1, -1, 37, -1, -1, -1, -1, 37, 37, 37, 37, -1, -1, 37, -1, -1, -1, 38, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, 37, -1, -1, 37 },
            { -1, -1, -1, -1, -1, -1, -1, 42, 41, -1, -1, -1, -1, -1, -1, 40, 40, 40, 40, -1, -1, 43, -1, -1, -1, -1, 40, 40, -1, -1, -1, -1, -1, 40, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, 44, 44, 44, -1, -1, -1, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, -1, 44, -1 },
            { -1, -1, -1, -1, 45, -1, -1, -1, -1, 45, 45, 45, 45, -1, -1, 45, -1, -1, -1, 45, 45, -1, 46, 46, 46, 46, -1, -1, -1, -1, 45, 45, -1, -1, 45 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 47, 48, 49, 50, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 51, 51, 51, 51, -1, -1, -1, -1, -1, -1, -1, 51, 51, -1, -1, -1, -1, -1, 51, -1 },
            { -1, -1, -1, -1, 52, -1, -1, -1, -1, 52, 52, 52, 52, -1, -1, 52, -1, -1, -1, 52, 52, -1, 52, 52, 52, 52, 53, 54, -1, -1, 52, 52, -1, -1, 52 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 55, 55, 55, 55, -1, -1, -1, -1, -1, -1, -1, 55, 55, -1, -1, -1, -1, -1, 55, -1 },
            { -1, -1, -1, -1, 56, -1, -1, -1, -1, 56, 56, 56, 56, -1, -1, 56, -1, -1, -1, 56, 56, -1, 56, 56, 56, 56, 56, 56, 57, 58, 56, 56, -1, -1, 56 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 59, 60, 61, 62, -1, -1, -1, -1, -1, -1, -1, 64, 65, -1, -1, -1, -1, -1, 63, -1 }
        };

        int[][] PRODUCTIONS = {
            {  3, 37,  4 },
            { 39, 32, 38 },
            { 37 },
            {  0 },
            { 41, 40 },
            { 45 },
            { 46 },
            { 47 },
            { 48 },
            { 33, 62 },
            {  0 },
            { 44 },
            { 45 },
            { 46 },
            { 47 },
            { 48 },
            { 41, 33, 62 },
            { 10, 34, 50, 35 },
            { 52, 16, 51 },
            { 31, 50 },
            {  0 },
            { 19, 31 },
            {  0 },
            { 11, 34, 53, 35 },
            { 12, 34, 53, 35 },
            { 57, 58, 59 },
            {  5, 62, 55 },
            {  0 },
            {  6, 62, 55, 58 },
            {  7, 55,  4 },
            {  4 },
            { 13, 55, 49 },
            { 60 },
            { 61 },
            { 15, 62 },
            { 14, 62 },
            { 64, 63 },
            {  0 },
            { 20, 64, 63 },
            { 21, 64, 63 },
            { 65 },
            {  9 },
            {  8 },
            { 22, 64 },
            { 68, 66 },
            {  0 },
            { 67, 68 },
            { 23 },
            { 24 },
            { 25 },
            { 26 },
            { 70, 69 },
            {  0 },
            { 27, 70, 69 },
            { 28, 70, 69 },
            { 72, 71 },
            {  0 },
            { 29, 72, 71 },
            { 30, 72, 71 },
            { 16 },
            { 17 },
            { 18 },
            { 19 },
            { 34, 62, 35 },
            { 27, 72 },
            { 28, 72 },
            { 43, 32, 56 },
            { 55 },
            {  0 },
            { 62, 54 },
            { 31, 53 },
            {  0 },
            { 16, 42 },
            { 31, 41 },
            {  0 }
        };

        String[] PARSER_ERROR = {
            "",
            "esperado EOF",
            "esperado palavra_reservada",
            "esperado pr_main",
            "esperado pr_end",
            "esperado pr_if",
            "esperado pr_elif",
            "esperado pr_else",
            "esperado pr_false",
            "esperado pr_true",
            "esperado pr_read",
            "esperado pr_write",
            "esperado pr_writeln",
            "esperado pr_repeat",
            "esperado pr_until",
            "esperado pr_while",
            "esperado id",
            "esperado constante_int",
            "esperado constante_float",
            "esperado constante_string",
            "esperado &&",
            "esperado ||",
            "esperado !",
            "esperado ==",
            "esperado !=",
            "esperado <",
            "esperado >",
            "esperado +",
            "esperado -",
            "esperado *",
            "esperado /",
            "esperado ,",
            "esperado ;",
            "esperado =",
            "esperado (",
            "esperado )",
            "esperado main",
            "esperado if read write writeln repeat id", // "<list_instr> inválido",
            "esperado end if read write writeln repeat id", // "<lista_instr1> inválido",
            "esperado if read write writeln repeat id", // "<instrucao> inválido",
            "esperado ; =", // "<instrucao1> inválido",
            "esperado id", // <lista_id> inválido
            "esperado , = ;", // "<lista_id1> inválido",
            "esperado if read write writeln repeat id", // "<comando> inválido",
            "esperado id", // "<atribuicao> inválido",
            "esperado read", // "<input> inválido",
            "esperado write writeln", // "<output> inválido",
            "esperado if", // "<selecao> inválido",
            "esperado repeat", // "<repeticao> inválido",
            "esperado until while", // "<repeticao1> inválido",
            "esperado id constante_string", // "<lista_inputs> inválido",
            "esperado , )", // "<lista_inputs1> inválido",
            "esperado id constante_string", // "<inicio_input> inválido",
            "esperado expressao", // "<lista_expr> inválido",
            "esperado , )", // "<lista_expr1> inválido",
            "esperado if read write writeln repeat id", // "<lista_comandos> inválido",
            "esperado end if elif else read write writeln repeat until while id", // "<lista_comandos1> inválido",
            "esperado if", // "<if> inválido",
            "esperado end elif else", // "<elif> inválido",
            "esperado end else", // "<else> inválido",
            "esperado while", // "<repeat_while> inválido",
            "esperado until", // "<repeat_until> inválido",
            "esperado expressao", // "<expressao> inválido",
            "esperado expressao", // "<expressao1> inválido",
            "esperado expressao", // "<elemento> inválido",
            "esperado expressao", // "<relacional> inválido",
            "esperado expressao", // "<relacional1> inválido",
            "esperado == != > <", // "<operador_relacional> inválido",
            "esperado expressao", // "<aritmetica> inválido",
            "esperado expressao", // "<aritmetica1> inválido",
            "esperado expressao", // "<termo> inválido",
            "esperado expressao", // "<termo1> inválido",
            "esperado expressao", // "<fator> inválido"
        };
    }
}
