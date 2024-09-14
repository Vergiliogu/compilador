package com.domain.lexical;

public enum Word {

    EPSILON(0, "î"),
    DOLLAR(1, "$"),
    RESERVED_WORD(2, "palavra_reservada"),
    MAIN(3, "pr_main"),
    END(4, "pr_end"),
    IF(5, "pr_if"),
    ELIF(6, "pr_elif"),
    ELSE(7, "pr_else"),
    FALSE(8, "pr_false"),
    TRUE(9, "pr_true"),
    READ(10, "pr_read"),
    WRITE(11, "pr_write"),
    WRITELN(12, "pr_writeln"),
    REPEAT(13, "pr_repeat"),
    UNTIL(14, "pr_until"),
    WHILE(15, "pr_while"),
    IDENTIFIER(16, "identificador"),
    INTEGER(17, "constante_int"),
    FLOAT(18, "constante_float"),
    STRING(19, "constante_string"),
    AND(20, "&&"),
    OR(21, "||"),
    NOT(22, "!"),
    EQUALS(23, "=="),
    NOT_EQUALS(24, "!="),
    LESS_THAN(25, "<"),
    GREATER_THAN(26, ">"),
    PLUS(27, "+"),
    MINUS(28, "-"),
    TIMES(29, "*"),
    SLASH(30, "/"),
    COMMA(31, ","),
    SEMI_COLON(32, ";"),
    ASSIGN(33, "="),
    OPEN_PARENTHESIS(34, "("),
    CLOSE_PARENTHESIS(35, ")");

    private final int wordId;
    private final String word;

    Word(int wordId, String word) {
        this.wordId = wordId;
        this.word = word;
    }

    public static Word fromId(final int id) {
        for (Word v : Word.values())
            if (id == v.getId())
                return v;

        return null;
    }

    public int getId() {
        return wordId;
    }

    public String getWord() {
        return word;
    }
}
