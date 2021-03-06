package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.completion.CompletionServiceImpl;
import fr.alexpado.jda.services.completion.interfaces.ICompletionService;
import fr.alexpado.jda.services.completion.interfaces.IMatchingResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fr.alexpado.jda.service.completion.CompletionTestData.assertListEquals;
import static fr.alexpado.jda.service.completion.CompletionTestData.passThroughInput;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pass Through Completion")
public class PassThroughCompletionTests {

    @Test
    @DisplayName("Unfinished input")
    public void testPassThroughCompletionOnUnfinishedInput() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(passThroughInput());
        List<String>                results = service.complete("language switch j");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Trailing space")
    public void testPassThroughCompleteWithTrailingSpace() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(passThroughInput());
        List<String>                results = service.complete("language switch ");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("No trailing space")
    public void testPassThroughCompleteWithoutTrailingSpace() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(passThroughInput());
        List<String>                results = service.complete("language switch");
        assertListEquals(Collections.singletonList("switch"), results);
    }

    @Test
    @DisplayName("Should be empty")
    public void testPassThroughCompleteShouldBeEmpty() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(passThroughInput());
        List<String>                results = service.complete("language swit ja");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Should be present")
    public void testPassThroughMatchShouldBePresent() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(passThroughInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language switch java");
        assertTrue(results.isPresent());
    }

    @Test
    @DisplayName("Should have a parameter")
    public void testPassThroughMatchShouldHaveParameter() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(passThroughInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language switch yes");
        assertTrue(results.isPresent());
        assertEquals("yes", results.get().getParameter("lang"));
    }

    @Test
    @DisplayName("Should not be present (invalid)")
    public void testPassThroughMatchShouldNotBePresentWhenInvalid() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(passThroughInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language swit java");
        assertFalse(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (incomplete)")
    public void testPassThroughMatchShouldNotBePresentWhenIncomplete() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(passThroughInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language swit java");
        assertFalse(results.isPresent());
    }
}
