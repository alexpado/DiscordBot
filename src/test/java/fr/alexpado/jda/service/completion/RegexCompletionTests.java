package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.syntax.SyntaxService;
import fr.alexpado.jda.services.syntax.interfaces.IMatchingResult;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static fr.alexpado.jda.service.completion.CompletionTestData.REGEX_INPUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Regex Matching")
public class RegexCompletionTests {

    @Test
    @DisplayName("Numeric match")
    public void testRegexNumericMatch() {

        ISyntaxService<Integer>            service = new SyntaxService<>(REGEX_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("regex 5547");
        assertTrue(results.isPresent());
        assertEquals(1, results.get().getIdentifier());
    }

    @Test
    @DisplayName("Text match")
    public void testRegexTextMatch() {

        ISyntaxService<Integer>            service = new SyntaxService<>(REGEX_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("regex aabbcc");
        assertTrue(results.isPresent());
        assertEquals(2, results.get().getIdentifier());
    }

    @Test
    @DisplayName("Strict match")
    public void testRegexStrictMatch() {

        ISyntaxService<Integer>            service = new SyntaxService<>(REGEX_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("regex strict");
        assertTrue(results.isPresent());
        assertEquals(3, results.get().getIdentifier());
    }

}
