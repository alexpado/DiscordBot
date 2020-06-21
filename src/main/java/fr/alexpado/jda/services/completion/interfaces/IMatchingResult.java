package fr.alexpado.jda.services.completion.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * Interface representing a matching result.
 *
 * @param <T>
 *         Type for a completion identifier. A completion identifier can be any object, but make sure that you can
 *         identify them between each other, or the whole purpose of this would be defeated.
 *
 * @author alexpado
 */
public interface IMatchingResult<T> {

    /**
     * Get the completion identifier {@link T} resulting of this {@link IMatchingResult}.
     *
     * @return A completion identifier {@link T}
     */
    @NotNull
    T getIdentifier();

    /**
     * Retrieve the value of the provided dynamic completion argument.
     *
     * @param name
     *         Name of the dynamic completion argument.
     *
     * @return A {@link String}
     */
    String getParameter(String name);

}
