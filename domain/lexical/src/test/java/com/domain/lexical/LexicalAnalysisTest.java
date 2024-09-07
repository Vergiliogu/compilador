package com.domain.lexical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ArgumentsSources;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

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
        void shouldThrowForStringWithLineFeed() {
            String stringWithLineFeed =
                    """
                    "String
                    "
                    """;

            assertOutputToBe(
                    LexicalAnalysis.Messages.LEXICAL_ERROR + "linha 1: " + ScannerConstants.INVALID_STRING, stringWithLineFeed);
        }

        @Test
        void shouldThrowForInvalidSpecifier() {
            String stringWithInvalidSpecifier =
                    """
                    "Valor necessário: %a"
                    """;

            assertOutputToBe(
                    LexicalAnalysis.Messages.LEXICAL_ERROR + "linha 1: " + ScannerConstants.INVALID_STRING, stringWithInvalidSpecifier);
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
        void shouldThrowForInvalidSymbol(String invalidSymbol) {
            String errorMessage = String.format("%slinha 1: %s %s",
                    LexicalAnalysis.Messages.LEXICAL_ERROR,
                    invalidSymbol,
                    ScannerConstants.INVALID_SYMBOL);

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

            String errorMessage = String.format("%slinha 2: %s %s",
                    LexicalAnalysis.Messages.LEXICAL_ERROR,
                    "@",
                    ScannerConstants.INVALID_SYMBOL);

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
                    untill
                    """;

            String errorMessage = String.format("%slinha 1: %s %s",
                    LexicalAnalysis.Messages.LEXICAL_ERROR,
                    "untill",
                    ScannerConstants.INVALID_RESERVED_WORD);

            assertOutputToBe(errorMessage, invalidReservedWords);
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

    private void assertOutputToBe(String expectedOutput, String sourceCode) {
        String out = lexical.run(sourceCode);

        assertEquals(expectedOutput, out);
    }

    private void assertThatWasCompiledSuccessfully(String invalidSymbolInsideString) {
        assertOutputToBe(LexicalAnalysis.Messages.PROGRAM_COMPILED, invalidSymbolInsideString);
    }
}
