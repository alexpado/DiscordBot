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
import static fr.alexpado.jda.service.completion.CompletionTestData.fillerInput;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Filler Completion")
public class FillerCompletionTests {

    @Test
    @DisplayName("Unfinished input")
    public void testPassThroughCompletionOnUnfinishedInput() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(fillerInput());
        List<String>                results = service.complete("language message j");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Trailing space")
    public void testPassThroughCompleteWithTrailingSpace() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(fillerInput());
        List<String>                results = service.complete("language message ");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("No trailing space")
    public void testPassThroughCompleteWithoutTrailingSpace() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(fillerInput());
        List<String>                results = service.complete("language message");
        assertListEquals(Collections.singletonList("message"), results);
    }

    @Test
    @DisplayName("Should be empty")
    public void testPassThroughCompleteShouldBeEmpty() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(fillerInput());
        List<String>                results = service.complete("language messa ja");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Should be present")
    public void testPassThroughMatchShouldBePresent() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(fillerInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language message Hello You !");
        assertTrue(results.isPresent());
    }

    @Test
    @DisplayName("Should have a parameter")
    public void testPassThroughMatchShouldHaveParameter() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(fillerInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language message Hello You ! This is a filler for very long string at the end of a command :)");
        assertTrue(results.isPresent());
        assertEquals("Hello You ! This is a filler for very long string at the end of a command :)", results.get().getParameter("msg"));
    }

    @Test
    @DisplayName("Should not be present (invalid)")
    public void testPassThroughMatchShouldNotBePresentWhenInvalid() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(fillerInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language messa java");
        assertFalse(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (incomplete)")
    public void testPassThroughMatchShouldNotBePresentWhenIncomplete() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(fillerInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language swit java");
        assertFalse(results.isPresent());
    }

}
