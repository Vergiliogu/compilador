package compiler;

public interface Parser {

    int EPSILON  = 0;
    int DOLLAR = 1;
    int STRING_CONSTANT = 19;

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
            "esperado if read write writeln repeat id",                           // list_instr
            "esperado end if read write writeln repeat id",                       // lista_instr1
            "esperado if read write writeln repeat id",                           // instrucao
            "esperado ; =",                                                       // instrucao1
            "esperado id",                                                        // lista_i
            "esperado , = ;",                                                     // lista_id1
            "esperado if read write writeln repeat id",                           // comando
            "esperado id",                                                        // atribuicao
            "esperado read",                                                      // input
            "esperado write writeln",                                             // output
            "esperado if",                                                        // selecao
            "esperado repeat",                                                    // repeticao
            "esperado until while",                                               // repeticao1
            "esperado id constante_string",                                       // lista_inputs
            "esperado , )",                                                       // lista_inputs1
            "esperado id constante_string",                                       // inicio_input
            "esperado expressao",                                                 // lista_expr
            "esperado , )",                                                       // lista_expr1
            "esperado if read write writeln repeat id",                           // lista_comandos
            "esperado end if elif else read write writeln repeat until while id", // lista_comandos1
            "esperado if",                                                        // if
            "esperado end elif else",                                             // elif
            "esperado end else",                                                  // else
            "esperado while",                                                     // repeat_while
            "esperado until",                                                     // repeat_until
            "esperado expressao",                                                 // expressao
            "esperado expressao",                                                 // expressao1
            "esperado expressao",                                                 // elemento
            "esperado expressao",                                                 // relacional
            "esperado expressao",                                                 // relacional1
            "esperado == != > <",                                                 // operador_relacional
            "esperado expressao",                                                 // aritmetica
            "esperado expressao",                                                 // aritmetica1
            "esperado expressao",                                                 // termo
            "esperado expressao",                                                 // termo1
            "esperado expressao",                                                 // fator
    };
}
