package com.domain.lexical;

public enum Words {

    EPSILON (0),
    DOLLAR  (1),
    RESERVED_WORD(2),
    MAIN(3),
    END(4),
    IF(5),
    ELIF(6),
    ELSE(7),
    FALSE(8),
    TRUE(9),
    READ(10),
    WRITE(11),
    WRITELN(12),
    REPEAT(13),
    UNTIL(14),
    WHILE(15),
    IDENTIFIER(16),
    INTEGER(17),
    FLOAT(18),
    STRING(19),
    AND(20),
    OR(21),
    NOT(22),
    EQUALS(23),
    NOT_EQUALS(24),
    LESS_THAN(25),
    GREATER_THAN(26),
    PLUS(27),
    MINUS(28),
    TIMES(29),
    SLASH(30),
    COMMA(31),
    SEMI_COLON(32),
    ASSIGN(33),
    OPEN_PARENTHESIS(34),
    CLOSE_PARENTHESIS(35);

    private final int word;

    Words(int word) {
        this.word = word;
    }

    public int get() {
        return word;
    }
}
