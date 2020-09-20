package fr.alexpado.jda.services.reactions.interfaces;

import net.dv8tion.jda.api.entities.Message;

import java.util.function.Consumer;

public interface IReactionListener {

    /**
     * Retrieve the {@link Message} on which this {@link IReactionListener} is active.
     *
     * @return A {@link Message} instance.
     */
    Message getMessage();

    /**
     * Add an action to this {@link IReactionListener}.
     *
     * @param emote
     *         The emote that will trigger the action.
     * @param action
     *         The action to execute.
     */
    void addAction(String emote, Consumer<IReactionAction> action);

    /**
     * Register this {@link IReactionListener} and start the timeout timer.
     */
    void start();

    /**
     * Unregister this {@link IReactionListener} and stop the timeout timer.
     */
    void stop();

    /**
     * Reset the timeout value of this {@link IReactionListener}.
     */
    void resetTimer();

}
