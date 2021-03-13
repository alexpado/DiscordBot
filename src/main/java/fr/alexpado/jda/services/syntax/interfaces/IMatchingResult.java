package fr.alexpado.jda.services.syntax.interfaces;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Interface representing a matching result.
 *
 * @param <T>
 *         Type of the identifier. An identifier can be anything, but should be easily distinguishable from other
 *         identifiers.
 */
public interface IMatchingResult<T> {

    /**
     * Retrieve the completion identifier {@link T} of this {@link IMatchingResult}.
     *
     * @return A completion identifier {@link T}.
     */
    @NotNull T getIdentifier();

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
    @NotNull Optional<String> getParameter(@NotNull String name);

}
