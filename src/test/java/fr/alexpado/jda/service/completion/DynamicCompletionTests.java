package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.completion.CompletionServiceImpl;
import fr.alexpado.jda.services.completion.interfaces.ICompletionService;
import fr.alexpado.jda.services.completion.interfaces.IMatchingResult;
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

        ICompletionService<Integer> service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        List<String>                results = service.complete("language switch j");
        assertListEquals(Arrays.asList("java", "javascript"), results);
    }

    @Test
    @DisplayName("Trailing space")
    public void testDynamicCompleteWithTrailingSpace() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        List<String>                results = service.complete("language switch ");
        assertListEquals(Arrays.asList("java", "php", "python", "javascript", "kotlin", "c#"), results);
    }

    @Test
    @DisplayName("No trailing space")
    public void testDynamicCompleteWithoutTrailingSpace() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        List<String>                results = service.complete("language switch");
        assertListEquals(Collections.singletonList("switch"), results);
    }

    @Test
    @DisplayName("Should be empty")
    public void testDynamicCompleteShouldBeEmpty() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        List<String>                results = service.complete("language swit ja");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Should be present")
    public void testDynamicMatchShouldBePresent() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language switch java");
        assertTrue(results.isPresent());
    }

    @Test
    @DisplayName("Should have a parameter")
    public void testDynamicMatchShouldHaveParameter() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language switch java");
        assertTrue(results.isPresent());
        assertEquals(results.get().getParameter("lang"), "java");
    }

    @Test
    @DisplayName("Should not be present (invalid)")
    public void testDynamicMatchShouldNotBePresentWhenInvalid() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language swit java");
        assertFalse(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (incomplete)")
    public void testDynamicMatchShouldNotBePresentWhenIncomplete() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language swit java");
        assertFalse(results.isPresent());
    }

}
