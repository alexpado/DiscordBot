package fr.alexpado.jda.services.tools.interfaces;

import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Interface representing a {@link MessageReactionAddEvent} listener which can associate an emote to a specific action.
 * <p>
 * Most of the features exposed here requires the {@link Permission#MESSAGE_MANAGE} permission.
 *
 * @author alexpado
 */
public interface IReactionListener {

    /**
     * Associate the provided emote to the specified {@link IReactionAction} consumer.
     *
     * @param emote
     *         The emote to associate
     * @param action
     *         The action to execute when a reaction uses the emote.
     */
    void addAction(@NotNull String emote, @NotNull Consumer<IReactionAction> action);

    /**
     * Dissociate the provided emote from this listener and the message.
     *
     * @param emote
     *         The emote to dissociate.
     */
    void removeAction(@NotNull String emote);

    /**
     * Start the internal timer for this listener and start listening for incoming {@link MessageReactionAddEvent}
     * events.
     *
     * @throws IllegalStateException
     *         Threw if {@link #getMessage()} is <code>null</code>.
     */
    void start();

    /**
     * Reset the internal timer to its initial value for this {@link IReactionListener}.
     */
    void resetTimer();

    /**
     * Cancel the internal timer and immediately stop this {@link IReactionListener}. This is also used by the internal
     * timer when the timeout is reached.
     */
    void timeout();

    /**
     * Retrieve the possibly <code>null</code> {@link Message} on which this {@link IReactionListener} should listen
     * for
     * incoming {@link MessageReactionAddEvent}.
     * <p>
     * This can be <code>null</code> if the {@link IReactionListener} has been instantiated without providing the {@link
     * Message} which will be given by the {@link ICommandHandler} implementation later on.
     *
     * @return The possibly <code>null</code> {@link Message}.
     */
    @Nullable Message getMessage();

    /**
     * Define the {@link Message} on which this {@link IReactionListener} should listen for incoming {@link
     * MessageReactionAddEvent}.
     *
     * @param message
     *         The {@link Message}.
     */
    void setMessage(@NotNull Message message);
}
