package com.domain.lexical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexicalAnalyserTest {

    private LexicalAnalyser lexical;

    @BeforeEach
    void setup() {
        lexical = new LexicalAnalyser();
    }

    @Nested
    class Strings {

        @Test
        void shouldThrowForStringWithLineFeed() {
            String stringWithLineFeed =
                    """
                    "String
                    "
                    """;

            assertOutputToBe(String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 1, ScannerConstants.INVALID_STRING), stringWithLineFeed);
        }

        @Test
        void shouldThrowForInvalidSpecifier() {
            String stringWithInvalidSpecifier =
                    """
                    "Valor necessário: %a"
                    """;

            assertOutputToBe(String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 1, ScannerConstants.INVALID_STRING), stringWithInvalidSpecifier);
        }

        @Test
        void shouldPassForAnyChar() {
            String validString =
                    """
                    "QWERTYUIOPASDFGHJKLZXCVBNM \
                    qwertyuiopasdfghjklzxcvbnm \
                    1234567890 \
                    !@#$%x^&*()_+-={}[]:;'>?./,<|\\~` \
                    "
                    """;

            assertThatWasCompiledSuccessfully(validString);
        }

        @Test
        void shouldPassForValidSpecifier() {
            String validString =
                    """
                    "Numero digitado é %x"
                    """;

            assertThatWasCompiledSuccessfully(validString);
        }

        @Test
        void shouldThrowForLineFeedAtLineTwo() {
            String stringWithLineFeed =
                    """
                    i_a
                    "String"
                    i_a2
                    "Valido
                    "
                    """;

            assertOutputToBe(String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 4, ScannerConstants.INVALID_STRING), stringWithLineFeed);
        }

    }

    @Nested
    class Symbols {

        @ParameterizedTest
        @ValueSource(strings = {
                "{", "[", "}", "]", "\\", "|", ".",
                "_", "#" ,"$", "@", "^", "&", "`", "~"
        })
        void shouldThrowForInvalidSymbol(String invalidSymbol) {
            String errorMessage = String.format(
                    LexicalAnalyser.Messages.LEXICAL_ERROR,
                    1,
                    invalidSymbol + " " + ScannerConstants.INVALID_SYMBOL);

            assertOutputToBe(
                    errorMessage, invalidSymbol);
        }

        @Test
        void shouldPassForInvalidSymbolInsideString() {
            String invalidSymbolInsideString =
                    """
                    "String@"
                    """;

            assertThatWasCompiledSuccessfully(invalidSymbolInsideString);
        }

        @Test
        void shouldThrowLineNumberForInvalidSymbol() {
            String invalidSymbol =
                    """
                    "String"
                    @
                    """;

            String errorMessage = String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 2, "@ " + ScannerConstants.INVALID_SYMBOL);

            assertOutputToBe(
                    errorMessage, invalidSymbol);
        }

    }

    @Nested
    class ReservedWords {

        @Test
        void shouldThrowForInvalidReservedWord() {
            String invalidReservedWords =
                    """
                    do <-- is not a reserved word
                    """;

            String errorMessage = String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 1, "do " +  ScannerConstants.INVALID_RESERVED_WORD);

            assertOutputToBe(errorMessage, invalidReservedWords);
        }


        @Test
        void shouldPassForBooleanOperators() {
            String booleanOperators =
                    """
                    && || && || && &&
                    """;

            assertThatWasCompiledSuccessfully(booleanOperators);
        }

        @ParameterizedTest
        @MethodSource("specialCasesKeys")
        void shouldPassForValidReservedWord(String validReservedWord) {
            assertThatWasCompiledSuccessfully(validReservedWord);
        }

        static Stream<String> specialCasesKeys() {
            return Stream.of(ScannerConstants.SPECIAL_CASES_KEYS);
        }

    }

    @Nested
    class Identifiers {

        @Test
        void shouldThrowForInvalidIdentifier() {
            String invalidIdentifier = """
                    i_1
                    """;

            String errorMessage = String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 1, "i_1 " + ScannerConstants.INVALID_IDENTIFIER);

            assertOutputToBe(errorMessage, invalidIdentifier);
        }

        // TODO: is this correct? or should be the whole id?
        @Test
        void shouldThrowForInvalidIdentifierWithTwoOrMoreChars() {
            String invalidIdentifier = "i_1123123123123123";

            String errorMessage = String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 1, "i_1 " + ScannerConstants.INVALID_IDENTIFIER);

            assertOutputToBe(errorMessage, invalidIdentifier);
        }

        @Test
        void shouldPassForValidIdentifier() {
            String validIdentifier =
                    """
                    i_int2
                    s_string
                    f_float1
                    b_booleanTouF
                    i_AaA123
                    f_a111A
                    """;

            assertThatWasCompiledSuccessfully(validIdentifier);
        }

    }

    @Nested
    class BlockComments {

        @Test
        void shouldThrowForInvalidBlockComment() {
            String invalidBlockComment =
                    """
                    i_a
                    b_a
                    >@
                    teste
                    teste
                    teste@
                    @<
                    """;

            String errorMessage = String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 3, ScannerConstants.INVALID_BLOCK_COMENT);

            assertOutputToBe(errorMessage, invalidBlockComment);
        }

        @Test
        void shouldThrowForInvalidBlockCommentAfterValidOne() {
            String invalidBlockComment =
                    """
                    >@
                    test
                    test
                    test
                    @<
                    
                    >@
                    test
                    test
                    test@
                    @<
                    """;

            String errorMessage = String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 7, ScannerConstants.INVALID_BLOCK_COMENT);

            assertOutputToBe(errorMessage, invalidBlockComment);
        }

    }

    @Test
    void shouldPassWithValidSourceCode() {
        String validSourceCode =
                """
                i_int2
                s_string
                f_float1
                
                >@
                this is a valid block comment
                @<
                
                b_booleanTouF
                i_AaA123
                f_a111A
                
                if (i_a && "teste string") repeat until
                
                1,0
                1124124
                
                while (i_a) (
                    write "teste 123 teste 456 %x %x %x"
                )
                
                >@
                this is another valid block comment
                
                if (f_a1 < 10,0) read (1)
                
                with more lines, actually
                @<
                """;

        assertThatWasCompiledSuccessfully(validSourceCode);
    }

    @Test
    void shouldThrowWithInvalidSourceCode() {
        String invalidSourceCode =
                """                
                b_booleanTouF
                
                1,0
                2,0
                1124124
                
                >@
                this is a invalid block comment
                
                test
                
                test
                
                @ <-- error here
                @<
                """;

        String errorMessage = String.format(LexicalAnalyser.Messages.LEXICAL_ERROR, 7, ScannerConstants.INVALID_BLOCK_COMENT);

        assertOutputToBe(errorMessage, invalidSourceCode);
    }

    private void assertOutputToBe(String expectedOutput, String sourceCode) {
        String out = lexical.run(sourceCode);

        assertEquals(expectedOutput, out);
    }

    private void assertThatWasCompiledSuccessfully(String invalidSymbolInsideString) {
        assertOutputToBe(LexicalAnalyser.Messages.PROGRAM_COMPILED, invalidSymbolInsideString);
    }
}
