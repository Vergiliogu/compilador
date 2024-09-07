package com.domain.lexical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexicalAnalysisTest {

    private LexicalAnalysis lexical;

    @BeforeEach
    void setup() {
        lexical = new LexicalAnalysis();
    }

    @Nested
    class Strings {

        @Test
        void mustThrowForStringWithLineFeed() {
            String stringWithLineFeed =
                    """
                    "String
                    "
                    """;

            assertOutputToBe(
                    LexicalAnalysis.Messages.LEXICAL_ERROR + "linha 1: " + ScannerConstants.INVALID_STRING, stringWithLineFeed);
        }

        @Test
        void mustThrowForInvalidSpecifier() {
            String stringWithInvalidSpecifier =
                    """
                    "Valor necessário: %a"
                    """;

            assertOutputToBe(
                    LexicalAnalysis.Messages.LEXICAL_ERROR + "linha 1: " + ScannerConstants.INVALID_STRING, stringWithInvalidSpecifier);
        }

        @Test
        void mustPassForAnyChar() {
            String validString =
                    """
                    "QWERTYUIOPASDFGHJKLZXCVBNM \
                    qwertyuiopasdfghjklzxcvbnm \
                    1234567890 \
                    !@#$%x^&*()_+-={}[]:;'>?./,<|\\~` \
                    "
                    """;

            assertOutputToBe(
                    LexicalAnalysis.Messages.PROGRAM_COMPILED, validString);
        }

        @Test
        void mustPassForValidSpecifier() {
            String validString =
                    """
                    "Numero digitado é %x"
                    """;

            assertOutputToBe(
                    LexicalAnalysis.Messages.PROGRAM_COMPILED, validString);
        }

        @Test
        void mustThrowForLineFeedAtLineTwo() {
            String stringWithLineFeed =
                    """
                    i_a
                    "String"
                    i_a2
                    "Valido
                    "
                    """;

            assertOutputToBe(
                    LexicalAnalysis.Messages.LEXICAL_ERROR + "linha 4: " + ScannerConstants.INVALID_STRING, stringWithLineFeed);
        }

    }

    @Nested
    class Symbols {

        @ParameterizedTest
        @ValueSource(strings = {
                "{", "[", "}", "]", "\\", "|", ".",
                "_", "#" ,"$", "@", "^", "&", "`", "~"
        })
        void mustThrowForInvalidSymbol(String invalidSymbol) {
            assertOutputToBe(
                    String.format("%slinha 1: %s %s", LexicalAnalysis.Messages.LEXICAL_ERROR, invalidSymbol, ScannerConstants.INVALID_SYMBOL), invalidSymbol);
        }

        @Test
        void mustPassForInvalidSymbolInsideString() {
            String invalidSymbolInsideString =
                    """
                    "String@"
                    """;

            assertOutputToBe(LexicalAnalysis.Messages.PROGRAM_COMPILED, invalidSymbolInsideString);
        }

        @Test
        void mustThrowLineNumberForInvalidSymbol() {
            String invalidSymbol =
                    """
                    "String"
                    @
                    """;

            assertOutputToBe(
                    String.format("%slinha 2: %s %s", LexicalAnalysis.Messages.LEXICAL_ERROR, "@", ScannerConstants.INVALID_SYMBOL), invalidSymbol);
        }

    }

    private void assertOutputToBe(String expectedOutput, String sourceCode) {
        String out = lexical.run(sourceCode);

        assertEquals(expectedOutput, out);
    }
}
