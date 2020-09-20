package fr.alexpado.jda.services.reactions.interfaces;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

/**
 * Interface representing an action triggered by an {@link IReactionListener}.
 *
 * @author alexpado
 */
public interface IReactionAction {

    /**
     * Retrieve the {@link IReactionListener} that trigger this {@link IReactionAction}.
     *
     * @return An {@link IReactionListener} instance.
     */
    @NotNull IReactionListener getListener();

    /**
     * Retrieve the {@link Message} on which this {@link IReactionAction} has been triggered.
     *
     * @return A {@link Message} instance.
     */
    @NotNull Message getMessage();

    /**
     * Retrieve the {@link User} that triggered this {@link IReactionAction}.
     *
     * @return An {@link User} instance.
     */
    @NotNull User getUser();

    /**
     * Retrieve the {@link MessageReaction} instance containing all information about the reaction.
     *
     * @return A {@link MessageReaction}.
     */
    @NotNull MessageReaction getReaction();

    /**
     * Remove the reaction from the message. This will affect all reactions added by the {@link User}.
     *
     * @return A {@link RestAction} instance.
     *
     * @see MessageReaction#removeReaction(User).
     */
    @NotNull RestAction<Void> removeReaction();

}
