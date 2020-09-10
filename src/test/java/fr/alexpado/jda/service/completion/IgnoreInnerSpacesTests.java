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

import static fr.alexpado.jda.service.completion.CompletionTestData.assertListEquals;
import static fr.alexpado.jda.service.completion.CompletionTestData.simpleInput;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Trim inner spaces")
public class IgnoreInnerSpacesTests {

    @Test
    @DisplayName("Unfinished input")
    public void testSimpleCompleteOnUnfinishedInput() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(simpleInput());
        List<String>                results = service.complete("language     switch     j");
        assertListEquals(Arrays.asList("java", "javascript"), results);
    }

    @Test
    @DisplayName("Trailing space")
    public void testSimpleCompleteWithTrailingSpace() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(simpleInput());
        List<String>                results = service.complete("language   switch ");
        assertListEquals(Arrays.asList("java", "php", "python", "javascript", "kotlin", "c#"), results);
    }

    @Test
    @DisplayName("No trailing space")
    public void testSimpleCompleteWithoutTrailingSpace() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(simpleInput());
        List<String>                results = service.complete("language      switch");
        assertListEquals(Collections.singletonList("switch"), results);
    }

    @Test
    @DisplayName("Should be empty")
    public void testSimpleCompleteShouldBeEmpty() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(simpleInput());
        List<String>                results = service.complete("language      swit    ja");
        assertListEquals(Collections.emptyList(), results);
    }

    @Test
    @DisplayName("Should be present")
    public void testSimpleMatchShouldBePresent() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(simpleInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language    switch     java");
        assertTrue(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (invalid)")
    public void testSimpleMatchShouldNotBePresentWhenInvalid() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(simpleInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language     swit    java");
        assertFalse(results.isPresent());
    }

    @Test
    @DisplayName("Should not be present (incomplete)")
    public void testSimpleMatchShouldNotBePresentWhenIncomplete() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(simpleInput());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language    swit     java");
        assertFalse(results.isPresent());
    }

}
