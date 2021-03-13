package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.syntax.SyntaxService;
import fr.alexpado.jda.services.syntax.SyntaxUtils;
import fr.alexpado.jda.services.syntax.interfaces.IMatchingResult;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxContainer;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fr.alexpado.jda.service.completion.CompletionTestData.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dynamic Completion")
public class DynamicCompletionTests {

    @Test
    @DisplayName("Unfinished input")
    public void testDynamicCompletionOnUnfinishedInput() {

        ISyntaxService<Integer> service = new SyntaxService<>(DYNAMIC_INPUT);
        List<String>            results = service.complete("language switch j");
        assertListEquals(Arrays.asList("java", "javascript"), results);
    }

    @Test
    @DisplayName("Trailing space")
    public void testDynamicCompleteWithTrailingSpace() {

        ISyntaxService<Integer> service = new SyntaxService<>(DYNAMIC_INPUT);
        List<String>                results = service.complete("language switch ");
        assertListEquals(Arrays.asList("java", "php", "python", "javascript", "kotlin", "c#"), results);
    }

    @Test
    @DisplayName("No trailing space")
    public void testDynamicCompleteWithoutTrailingSpace() {

        ISyntaxService<Integer> service = new SyntaxService<>(DYNAMIC_INPUT);
        List<String>                results = service.complete("language switch");
        assertListEquals(Collections.singletonList("switch"), results);
    }

    @Test
    @DisplayName("Should be empty")
    public void testDynamicCompleteShouldBeEmpty() {
        ISyntaxService<Integer> service = new SyntaxService<>(DYNAMIC_INPUT);
        List<String>                results = service.complete("language swit ja");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Should be present")
    public void testDynamicMatchShouldBePresent() {

        ISyntaxService<Integer>            service = new SyntaxService<>(DYNAMIC_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language switch java");
        assertTrue(results.isPresent());
    }

    @Test
    @DisplayName("Should have a parameter")
    public void testDynamicMatchShouldHaveParameter() {
        ISyntaxService<Integer> service = new SyntaxService<>(DYNAMIC_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language switch java");
        assertTrue(results.isPresent());
        assertEquals("java", results.get().getParameter("lang").orElse(null));
    }

    @Test
    @DisplayName("Should not be present (invalid)")
    public void testDynamicMatchShouldNotBePresentWhenInvalid() {

        ISyntaxService<Integer> service = new SyntaxService<>(DYNAMIC_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language swit java");
        assertFalse(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (incomplete)")
    public void testDynamicMatchShouldNotBePresentWhenIncomplete() {

        ISyntaxService<Integer> service = new SyntaxService<>(DYNAMIC_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language swit java");
        assertFalse(results.isPresent());
    }

}
