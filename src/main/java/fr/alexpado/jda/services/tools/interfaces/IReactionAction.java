package fr.alexpado.jda.services.tools.interfaces;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public interface IReactionAction {

    /**
     * Retrieve the {@link IReactionListener} to which this {@link IReactionAction} is associated.
     *
     * @return An {@link IReactionListener} implementation.
     */
    IReactionListener getListener();

    /**
     * Retrieve the {@link Message} to which this {@link IReactionAction} is associated.
     *
     * @return The {@link Message}.
     */
    Message getMessage();

    /**
     * Retrieve the {@link User} to which this {@link IReactionAction} is associated.
     *
     * @return The {@link User}.
     */
    User getUser();

    /**
     * Retrieve the {@link MessageReaction} that triggered this {@link IReactionAction}.
     *
     * @return The {@link MessageReaction}.
     */
    MessageReaction getReaction();

    /**
     * Tries to remove the {@link MessageReaction} from the {@link Message}.
     * <p>
     * NOTE: This require the {@link Permission#MESSAGE_MANAGE} permission.
     */
    void removeReaction();
}
