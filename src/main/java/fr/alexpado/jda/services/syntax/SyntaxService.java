package fr.alexpado.jda.services.syntax;

import fr.alexpado.jda.services.syntax.interfaces.IMatchingResult;
import fr.alexpado.jda.services.syntax.interfaces.ISyntax;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxContainer;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxService;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SyntaxService<T> implements ISyntaxService<T> {

    private static final Pattern                  MULTIPLE_SPACES = Pattern.compile(" +");
    private final        Map<T, ISyntaxContainer> identifierMap;

    public SyntaxService(Map<T, ISyntaxContainer> identifierMap) {

        this.identifierMap = identifierMap;
    }

    public static String getName(String value, String start, String end) {

        if (isEncapsulated(value, start, end)) {
            return value.substring(start.length(), value.length() - end.length());
        }
        throw new IllegalArgumentException("Invalid name.");
    }

    public static boolean isEncapsulated(String value, String start, String end) {

        return value.startsWith(start) && value.endsWith(end) && (value.length() - start.length() - end.length()) > 0;
    }

    /**
     * Prepare the given user's input for matching or completion handling.
     *
     * @param data
     *         The user's input to prepare
     *
     * @return A {@link List} of strings containing the sanitized user's input
     */
    @Override
    public @NotNull List<String> prepareUserData(@NotNull String data) {

        List<String> input = new ArrayList<>(Arrays.asList(MULTIPLE_SPACES.matcher(data.trim())
                                                                          .replaceAll(" ")
                                                                          .split(" ")));

        if (data.endsWith(" ")) {
            input.add("");
        }

        return input;
    }

    /**
     * Retrieve a {@link List} of strings completing the user's input.
     *
     * @param data
     *         The user's input to complete.
     *
     * @return A {@link List} of strings.
     */
    @Override
    public @NotNull List<String> complete(@NotNull String data) {

        List<String> userData = this.prepareUserData(data);


        return this.identifierMap.values()
                                 .stream()
                                 .filter(container -> container.isCompletable(userData))
                                 .map(ISyntaxContainer::getCompletion)
                                 .flatMap(List::stream)
                                 .distinct()
                                 .collect(Collectors.toList());
    }

    /**
     * Retrieve an optional {@link IMatchingResult} for the given user's input. The returned optional value won't be
     * empty if one, and only one identifier matches the user's input.
     *
     * @param data
     *         The user's input to match.
     *
     * @return An optional {@link IMatchingResult} for the given user's input.
     *
     * @see ISyntaxContainer#isMatching(List)
     */
    @Override
    public Optional<IMatchingResult<T>> getMatchingResult(@NotNull String data) {

        List<String> userData = this.prepareUserData(data);

        List<T> identifiers = this.identifierMap.keySet()
                                                .stream()
                                                .filter(id -> this.identifierMap.get(id).isMatching(userData))
                                                .collect(Collectors.toList());

        if (identifiers.isEmpty()) {
            return Optional.empty();
        }

        if (identifiers.size() == 1) {
            T                identifier = identifiers.get(0);
            ISyntaxContainer container  = this.identifierMap.get(identifier);
            return Optional.of(this.createResult(identifier, container));
        }

        identifiers.sort(Comparator.comparing(this.identifierMap::get));

        // Let's check if the two first identifiers have a different order.
        T firstIdentifier  = identifiers.get(0);
        T secondIdentifier = identifiers.get(1);

        ISyntaxContainer firstContainer  = this.identifierMap.get(firstIdentifier);
        ISyntaxContainer secondContainer = this.identifierMap.get(secondIdentifier);

        if (firstContainer.getOrder() == secondContainer.getOrder()) {
            // Multiple matches of the same order occurred.
            return Optional.empty();
        }

        return Optional.of(this.createResult(firstIdentifier, firstContainer));
    }

    /**
     * Create a {@link IMatchingResult} for the provided {@link T} and {@link ISyntaxContainer}.
     *
     * @param identifier
     *         The identifier for which this {@link IMatchingResult} should be created.
     * @param container
     *         The {@link ISyntaxContainer} containing the data about the matching.
     *
     * @return An anonymous {@link IMatchingResult} instance.
     */
    private IMatchingResult<T> createResult(T identifier, ISyntaxContainer container) {

        return new IMatchingResult<>() {

            /**
             * Retrieve the completion identifier {@link T} of this {@link IMatchingResult}.
             *
             * @return A completion identifier {@link T}.
             */
            @Override
            public @NotNull T getIdentifier() {

                return identifier;
            }

            /**
             * Retrieve the completion argument matching the provided name.
             *
             * @param name
             *         The argument name.
             *
             * @return An optional value of the argument.
             *
             * @see ISyntax#getName()
             */
            @Override
            public @NotNull Optional<String> getParameter(@NotNull String name) {

                return Optional.ofNullable(container.getMatches().get(name));
            }
        };
    }
}
