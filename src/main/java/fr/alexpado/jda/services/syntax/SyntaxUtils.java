package fr.alexpado.jda.services.syntax;

import fr.alexpado.jda.services.commands.annotations.Command;
import fr.alexpado.jda.services.syntax.interfaces.ISyntax;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxContainer;
import fr.alexpado.jda.services.syntax.rules.*;

import java.util.*;
import java.util.stream.Collectors;

public class SyntaxUtils {

    /**
     * Convert the provided {@link String} into the corresponding {@link ISyntax}.
     * <p>
     * Will always default to {@link WordSyntax} if nothing matched.
     *
     * @param options
     *         The {@link Map} containing the options possible for each argument.
     * @param value
     *         The {@link String} to convert into {@link ISyntax}.
     *
     * @return The {@link ISyntax} matching the provided {@link String}.
     */
    public static ISyntax toSyntax(Map<String, List<String>> options, String value) {

        if (SyntaxService.isEncapsulated(value, "{", "}")) {
            String name = SyntaxService.getName(value, "{", "}");
            return new SelectiveSyntax(value, options.getOrDefault(name, Collections.emptyList()));
        } else if (SyntaxService.isEncapsulated(value, "/", "/")) {
            return new RegexSyntax(value);
        } else if (SyntaxService.isEncapsulated(value, "[", "]")) {
            return new EagerSyntax(value);
        } else if (value.endsWith("...")) {
            return new FillingSyntax(value);
        } else {
            return new WordSyntax(value);
        }
    }

    /**
     * Convert the provided {@link String}s into the corresponding {@link ISyntax}es.
     * <p>
     * Will always default to {@link WordSyntax} if nothing matched.
     *
     * @param options
     *         The {@link Map} containing the options possible for each argument.
     * @param values
     *         The {@link String}s to convert into {@link ISyntax}es.
     *
     * @return The {@link ISyntax}es matching the provided {@link String}s.
     */
    public static List<ISyntax> toSyntax(Map<String, List<String>> options, Collection<String> values) {

        return values.stream().map(value -> toSyntax(options, value)).collect(Collectors.toList());
    }

    /**
     * Convert the provided {@link Command} into the corresponding {@link ISyntaxContainer}.
     *
     * @param command
     *         The {@link Command} annotation to convert.
     *
     * @return An {@link ISyntaxContainer} implementation.
     */
    public static ISyntaxContainer toContainer(Map<String, List<String>> options, String command, int order) {

        Collection<String> rawSyntaxList = Arrays.asList(command.trim().split(" "));
        return new SyntaxContainer(toSyntax(options, rawSyntaxList), order);
    }
}
