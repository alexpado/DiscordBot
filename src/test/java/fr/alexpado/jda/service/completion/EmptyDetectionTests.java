package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.completion.CompletionServiceImpl;
import fr.alexpado.jda.services.completion.interfaces.ICompletionService;
import fr.alexpado.jda.services.completion.interfaces.IMatchingResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@DisplayName("Various Command Tests")
public class EmptyDetectionTests {

    @Test
    @DisplayName("Detect empty command")
    public void testEmptyDetectionCommand() {

        ICompletionService<Integer> service = new CompletionServiceImpl<>(new HashMap<>() {{
            this.put(1, Collections.emptyList());
            this.put(2, Collections.singletonList("language"));
        }});
        Optional<IMatchingResult<Integer>> matchingResult = service.getMatchingIdentifier("");

        Assertions.assertTrue(matchingResult.isPresent());
        IMatchingResult<Integer> result = matchingResult.get();
        Assertions.assertEquals(1, result.getIdentifier());

    }

}
