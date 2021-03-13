package fr.alexpado.jda.services.syntax.interfaces;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author alexpado
 */
public interface ISyntax {

    /**
     * Retrieve this {@link ISyntax}'s name.
     * <p>
     * The returned value may then be used to retrieve the value from {@link ISyntaxContainer#getMatches()}.
     *
     * @return This {@link ISyntax}'s name.
     */
    @NotNull String getName();

    /**
     * Check if the provided string perfectly matches this {@link ISyntax} rules.
     * <p>
     * Every implementation of this method should only return <code>true</code> if, and only if, the provided string
     * matches exactly the expected data format.
     * <p>
     * You may not use this method to check if the provided data matches partially the rules, for which {@link
     * #isCompletable(String)} is more appropriate.
     *
     * @param data
     *         The data to check against this {@link ISyntax} rules.
     *
     * @return True if the provided data exactly matches this {@link ISyntax} rules, false otherwise.
     */
    boolean isMatching(@NotNull String data);

    /**
     * Retrieve the last value that matched with {@link #isMatching(String)}. This may return an empty optional if the
     * last return value of {@link #isMatching(String)} was <code>false</code>.
     *
     * @return The last matched value.
     */
    Optional<String> getLastMatch();

    /**
     * Check if the provided string partially matches this {@link ISyntax} rules and can be auto-completed.
     * <p>
     * Every implementation of this method may behave similarly to {@link String#startsWith(String)} for better user
     * experience with the auto-completion.
     * <p>
     * If this {@link ISyntax} isn't completable, this method should always return <code>false</code>.
     *
     * @param data
     *         The data to check against this {@link ISyntax} rules.
     *
     * @return True if the provided data can be auto-completed, false otherwise.
     */
    boolean isCompletable(@NotNull String data);

    /**
     * Retrieve a {@link List} of strings that matched the auto-complete of this {@link ISyntax}. All strings returned
     * should be complete options that contains the original user's input, if applicable.
     *
     * @param data
     *         The data to complete using this {@link ISyntax} rules.
     *
     * @return A {@link List} of strings that matched the user's input.
     *
     * @see #isCompletable(String)
     */
    List<String> complete(@NotNull String data);

}