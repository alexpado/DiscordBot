package fr.alexpado.jda.services.syntax;

import fr.alexpado.jda.services.syntax.interfaces.ISyntax;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxContainer;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxService;
import fr.alexpado.jda.services.syntax.rules.FillingSyntax;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SyntaxContainer implements ISyntaxContainer {

    private final List<ISyntax>       syntaxList;
    private final Map<String, String> matches;
    private final List<String>        completion;
    private final Integer             order;

    /**
     * Create a new instance of this {@link ISyntaxContainer} implementation.
     *
     * @param syntaxList
     *         A {@link List} of {@link ISyntax}
     */
    public SyntaxContainer(List<ISyntax> syntaxList, int order) {

        this.syntaxList = syntaxList;
        this.matches    = new HashMap<>();
        this.completion = new ArrayList<>();
        this.order      = order;
    }

    /**
     * Return a {@link List} of strings completing the user's input.
     *
     * @return A {@link List}
     */
    @Override
    public @NotNull List<String> getCompletion() {

        return this.completion;
    }

    /**
     * Return a {@link Map} associating all dynamic arguments to their matching values present in the user's input.
     *
     * @return A {@link Map}.
     */
    @Override
    public @NotNull Map<String, String> getMatches() {

        return this.matches;
    }

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
    @Override
    public boolean isCompletable(@NotNull List<String> data) {

        this.completion.clear();

        if (data.size() > this.syntaxList.size()) {
            return false;
        }

        for (int i = 0 ; i < data.size() ; i++) {
            ISyntax syntax   = this.syntaxList.get(i);
            String  argument = data.get(i);
            boolean isLast   = i == data.size() - 1;

            if ((isLast && !syntax.isCompletable(argument)) || (!isLast && !syntax.isMatching(argument))) {
                return false;
            }

            if (isLast) {
                this.completion.addAll(syntax.complete(argument));
            }
        }

        return true;
    }

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
    @Override
    public boolean isMatching(@NotNull List<String> data) {

        this.matches.clear();

        for (int i = 0 ; i < data.size() ; i++) {
            ISyntax syntax   = this.syntaxList.get(i);
            String  argument = data.get(i);

            if (i == this.syntaxList.size() - 1 && data.size() > this.syntaxList.size()) {

                //noinspection InstanceofConcreteClass
                if (syntax instanceof FillingSyntax) {

                    Collection<String> contents = new ArrayList<>();

                    for (int j = i ; j < data.size() ; j++) {
                        contents.add(data.get(j));
                    }

                    this.matches.put(syntax.getName(), String.join(" ", contents));
                    return true;
                }

                return false;
            }

            if (!syntax.isMatching(argument)) {
                return false;
            }

            syntax.getLastMatch().ifPresent(value -> this.matches.put(syntax.getName(), value));
        }

        return true;
    }

    /**
     * Retrieve this {@link ISyntaxContainer} order among all other {@link ISyntaxContainer} that may be created by
     * {@link ISyntaxService}.
     *
     * @return The order of this {@link ISyntaxContainer}
     */
    @Override
    public int getOrder() {

        return this.order;
    }

    @Override
    public int compareTo(@NotNull ISyntaxContainer other) {

        return Integer.compare(this.getOrder(), other.getOrder());
    }
}
