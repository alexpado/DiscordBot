package fr.alexpado.jda.services.syntax.interfaces;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @author alexpado
 */
public interface ISyntaxContainer extends Comparable<ISyntaxContainer> {

    /**
     * Return a {@link List} of strings completing the user's input.
     *
     * @return A {@link List}
     */
    @NotNull List<String> getCompletion();

    /**
     * Return a {@link Map} associating all dynamic arguments to their matching values present in the user's input.
     *
     * @return A {@link Map}.
     */
    @NotNull Map<String, String> getMatches();

    /**
     * Check if the provided {@link List} of strings, which should be each part of the user's input, can be completed by
     * this {@link ISyntaxContainer}.
     * <p>
     * If this method returns <code>true</code>, results should be made available through the {@link #getCompletion()}
     * method.
     * <p>
     * Otherwise, if this method returns <code>false</code>, the {@link #getCompletion()} method should return an empty
     * {@link List}.
     *
     * @param data
     *         The data to check against this {@link ISyntaxContainer}.
     *
     * @return True if the provided {@link List} is a valid candidate for completion, false otherwise.
     */
    boolean isCompletable(@NotNull List<String> data);

    /**
     * Check if the provided {@link List} of strings, which should be each part of the user's input, matches this {@link
     * ISyntaxContainer}
     * <p>
     * If this method returns <code>true</code>, results should be made available through the {@link #getMatches()}
     * method.
     * <p>
     * Otherwise, if this method returns <code>false</code>, the {@link #getMatches()} method should return an empty
     * {@link List}.
     *
     * @param data
     *         The data to check against this {@link ISyntaxContainer}.
     *
     * @return True if the provided {@link List} matches this {@link ISyntaxContainer}.
     */
    boolean isMatching(@NotNull List<String> data);

    /**
     * Retrieve this {@link ISyntaxContainer} order among all other {@link ISyntaxContainer} that may be created by
     * {@link ISyntaxService}.
     *
     * @return The order of this {@link ISyntaxContainer}
     */
    int getOrder();
}