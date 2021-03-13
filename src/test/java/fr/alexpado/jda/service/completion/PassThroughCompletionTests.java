package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.syntax.SyntaxService;
import fr.alexpado.jda.services.syntax.interfaces.IMatchingResult;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fr.alexpado.jda.service.completion.CompletionTestData.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pass Through Completion")
public class PassThroughCompletionTests {

    @Test
    @DisplayName("Unfinished input")
    public void testPassThroughCompletionOnUnfinishedInput() {

        ISyntaxService<Integer> service = new SyntaxService<>(PASS_THROUGH_INPUT);
        List<String>            results = service.complete("language switch j");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Trailing space")
    public void testPassThroughCompleteWithTrailingSpace() {

        ISyntaxService<Integer> service = new SyntaxService<>(PASS_THROUGH_INPUT);
        List<String>                results = service.complete("language switch ");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("No trailing space")
    public void testPassThroughCompleteWithoutTrailingSpace() {

        ISyntaxService<Integer> service = new SyntaxService<>(PASS_THROUGH_INPUT);
        List<String>                results = service.complete("language switch");
        assertListEquals(Collections.singletonList("switch"), results);
    }

    @Test
    @DisplayName("Should be empty")
    public void testPassThroughCompleteShouldBeEmpty() {

        ISyntaxService<Integer> service = new SyntaxService<>(PASS_THROUGH_INPUT);
        List<String>                results = service.complete("language swit ja");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Should be present")
    public void testPassThroughMatchShouldBePresent() {

        ISyntaxService<Integer> service = new SyntaxService<>(PASS_THROUGH_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language switch java");
        assertTrue(results.isPresent());
    }

    @Test
    @DisplayName("Should have a parameter")
    public void testPassThroughMatchShouldHaveParameter() {

        ISyntaxService<Integer> service = new SyntaxService<>(PASS_THROUGH_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language switch yes");
        assertTrue(results.isPresent());
        assertEquals("yes", results.get().getParameter("lang").orElse(null));
    }

    @Test
    @DisplayName("Should not be present (invalid)")
    public void testPassThroughMatchShouldNotBePresentWhenInvalid() {

        ISyntaxService<Integer> service = new SyntaxService<>(PASS_THROUGH_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language swit java");
        assertFalse(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (incomplete)")
    public void testPassThroughMatchShouldNotBePresentWhenIncomplete() {

        ISyntaxService<Integer>            service = new SyntaxService<>(PASS_THROUGH_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language swit java");
        assertFalse(results.isPresent());
    }
}
