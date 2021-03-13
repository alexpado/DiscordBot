package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.syntax.SyntaxService;
import fr.alexpado.jda.services.syntax.interfaces.IMatchingResult;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fr.alexpado.jda.service.completion.CompletionTestData.FILLER_INPUT;
import static fr.alexpado.jda.service.completion.CompletionTestData.assertListEquals;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Filler Completion")
public class FillerCompletionTests {

    @Test
    @DisplayName("Unfinished input")
    public void testPassThroughCompletionOnUnfinishedInput() {

        ISyntaxService<Integer> service = new SyntaxService<>(FILLER_INPUT);
        List<String>            results = service.complete("language message j");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Trailing space")
    public void testPassThroughCompleteWithTrailingSpace() {

        ISyntaxService<Integer> service = new SyntaxService<>(FILLER_INPUT);
        List<String>            results = service.complete("language message ");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("No trailing space")
    public void testPassThroughCompleteWithoutTrailingSpace() {

        ISyntaxService<Integer> service = new SyntaxService<>(FILLER_INPUT);
        List<String>            results = service.complete("language message");
        assertListEquals(Collections.singletonList("message"), results);
    }

    @Test
    @DisplayName("Should be empty")
    public void testPassThroughCompleteShouldBeEmpty() {

        ISyntaxService<Integer> service = new SyntaxService<>(FILLER_INPUT);
        List<String>            results = service.complete("language messa ja");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Should be present")
    public void testPassThroughMatchShouldBePresent() {

        ISyntaxService<Integer>            service = new SyntaxService<>(FILLER_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language message Hello You !");
        assertTrue(results.isPresent());
    }

    @Test
    @DisplayName("Should have a parameter")
    public void testPassThroughMatchShouldHaveParameter() {

        ISyntaxService<Integer>            service = new SyntaxService<>(FILLER_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language message Hello You ! This is a filler for very long string at the end of a command :)");
        assertTrue(results.isPresent());
        assertEquals("Hello You ! This is a filler for very long string at the end of a command :)", results.get()
                                                                                                            .getParameter("msg")
                                                                                                            .orElse(null));
    }

    @Test
    @DisplayName("Should not be present (invalid)")
    public void testPassThroughMatchShouldNotBePresentWhenInvalid() {

        ISyntaxService<Integer>            service = new SyntaxService<>(FILLER_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language messa java");
        assertFalse(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (incomplete)")
    public void testPassThroughMatchShouldNotBePresentWhenIncomplete() {

        ISyntaxService<Integer>            service = new SyntaxService<>(FILLER_INPUT);
        Optional<IMatchingResult<Integer>> results = service.getMatchingResult("language swit java");
        assertFalse(results.isPresent());
    }

}
