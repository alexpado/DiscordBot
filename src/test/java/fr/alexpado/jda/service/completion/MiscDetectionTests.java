package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.syntax.SyntaxService;
import fr.alexpado.jda.services.syntax.interfaces.IMatchingResult;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static fr.alexpado.jda.service.completion.CompletionTestData.*;

@DisplayName("Various Command Tests")
public class MiscDetectionTests {

    @Test
    @DisplayName("Detect empty command")
    public void testEmptyDetectionCommand() {

        ISyntaxService<Integer>            service        = new SyntaxService<>(EMPTY_INPUT);
        Optional<IMatchingResult<Integer>> matchingResult = service.getMatchingResult("");

        Assertions.assertTrue(matchingResult.isPresent());
        IMatchingResult<Integer> result = matchingResult.get();
        Assertions.assertEquals(1, result.getIdentifier());
    }

    @Test
    @DisplayName("Detect collide with order - Word priority")
    public void testCollideOrderA() {

        ISyntaxService<Integer>            service        = new SyntaxService<>(COLLISION_INPUT_A);
        Optional<IMatchingResult<Integer>> matchingResult = service.getMatchingResult("collide help");

        Assertions.assertTrue(matchingResult.isPresent());
        IMatchingResult<Integer> result = matchingResult.get();
        Assertions.assertEquals(1, result.getIdentifier());
    }

    @Test
    @DisplayName("Detect collide with order - Eager priority")
    public void testCollideOrderB() {

        ISyntaxService<Integer>            service        = new SyntaxService<>(COLLISION_INPUT_B);
        Optional<IMatchingResult<Integer>> matchingResult = service.getMatchingResult("collide help");

        Assertions.assertTrue(matchingResult.isPresent());
        IMatchingResult<Integer> result = matchingResult.get();
        Assertions.assertEquals(2, result.getIdentifier());
    }

}
