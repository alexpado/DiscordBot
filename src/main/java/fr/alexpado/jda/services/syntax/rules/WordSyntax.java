package fr.alexpado.jda.services.syntax.rules;

import fr.alexpado.jda.services.syntax.interfaces.ISyntax;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WordSyntax implements ISyntax {

    private final     String name;
    private @Nullable String lastMatch;

    public WordSyntax(String name) {

        this.name      = name;
        this.lastMatch = null;
    }

    /**
     * Retrieve this {@link ISyntax}'s name.
     * <p>
     * The returned value may then be used to retrieve the value from {@link ISyntaxContainer#getMatches()}.
     *
     * @return This {@link ISyntax}'s name.
     */
    @Override
    public @NotNull String getName() {

        return this.name;
    }

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
    @Override
    public boolean isMatching(@NotNull String data) {

        if (this.name.equals(data)) {
            this.lastMatch = data;
            return true;
        }

        this.lastMatch = null;
        return false;
    }

    /**
     * Retrieve the last value that matched with {@link #isMatching(String)}. This may return an empty optional if the
     * last return value of {@link #isMatching(String)} was <code>false</code>.
     *
     * @return The last matched value.
     */
    @Override
    public Optional<String> getLastMatch() {

        return Optional.ofNullable(this.lastMatch);
    }

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
    @Override
    public boolean isCompletable(@NotNull String data) {

        return this.name.startsWith(data);
    }

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
    @Override
    public List<String> complete(@NotNull String data) {

        return Collections.singletonList(this.name);
    }
}
