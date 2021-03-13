package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.syntax.SyntaxService;
import fr.alexpado.jda.services.syntax.interfaces.IMatchingResult;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fr.alexpado.jda.service.completion.CompletionTestData.SIMPLE_INPUT;
import static fr.alexpado.jda.service.completion.CompletionTestData.assertListEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Simple completion")
public class SimpleCompletionTests {

    @Test
    @DisplayName("Unfinished input")
    public void testSimpleCompleteOnUnfinishedInput() {

        ISyntaxService<Integer> service = new SyntaxService<>(SIMPLE_INPUT);
        List<String>            results = service.complete("language switch j");
        assertListEquals(Arrays.asList("java", "javascript"), results);
    }

    @Test
    @DisplayName("Trailing space")
    public void testSimpleCompleteWithTrailingSpace() {

        ISyntaxService<Integer> service = new SyntaxService<>(SIMPLE_INPUT);
        List<String>            results = service.complete("language switch ");
        assertListEquals(Arrays.asList("java", "php", "python", "javascript", "kotlin", "c#"), results);
    }

    @Test
    @DisplayName("No trailing space")
    public void testSimpleCompleteWithoutTrailingSpace() {

        ISyntaxService<Integer> service = new SyntaxService<>(SIMPLE_INPUT);
        List<String>            results = service.complete("language switch");
        assertListEquals(Collections.singletonList("switch"), results);
    }

    @Test
    @DisplayName("Should be empty")
    public void testSimpleCompleteShouldBeEmpty() {

        ISyntaxService<Integer> service = new SyntaxService<>(SIMPLE_INPUT);
        List<String>            results = service.complete("language swit ja");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Should be present")
    public void testSimpleMatchShouldBePresent() {

        ISyntaxService<Integer>            service = new SyntaxService<>(SIMPLE_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language switch java");
        assertTrue(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (invalid)")
    public void testSimpleMatchShouldNotBePresentWhenInvalid() {

        ISyntaxService<Integer>            service = new SyntaxService<>(SIMPLE_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language swit java");
        assertFalse(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (incomplete)")
    public void testSimpleMatchShouldNotBePresentWhenIncomplete() {

        ISyntaxService<Integer>            service = new SyntaxService<>(SIMPLE_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language swit java");
        assertFalse(results.isPresent());
    }

}
