package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.completion.CompletionServiceImpl;
import fr.alexpado.jda.services.completion.interfaces.ICompletionService;
import fr.alexpado.jda.services.completion.interfaces.IMatchingResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static fr.alexpado.jda.service.completion.CompletionTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Flag Detection")
public class FlagDetectionTests {

    @Test
    @DisplayName("Flag Detection - Matching")
    public void testFlagDetectionMatching() {

        ICompletionService<Integer>        service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        Optional<IMatchingResult<Integer>> results = service.getMatchingIdentifier("language -r switch java");
        assertTrue(results.isPresent());
        assertEquals(results.get().getParameter("lang"), "java");
        assertTrue(results.get().getFlags().contains("r"));
    }

    @Test
    @DisplayName("Flag Detection - Don't mess up completion")
    public void testFlagDetectionCompletion() {
        ICompletionService<Integer> service = new CompletionServiceImpl<>(dynamicInput(), dynamicOptions());
        List<String>                results = service.complete("language -r switch -s j");
        assertListEquals(Arrays.asList("java", "javascript"), results);
    }

}
