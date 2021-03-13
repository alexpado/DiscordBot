package fr.alexpado.jda.services.tools.reaction;


import fr.alexpado.jda.services.tools.interfaces.IReactionAction;
import fr.alexpado.jda.services.tools.interfaces.IReactionListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public class ReactionAction implements IReactionAction {

    private final IReactionListener listener;
    private final Message           message;
    private final User              user;
    private final MessageReaction   reaction;

    ReactionAction(IReactionListener listener, Message message, User user, MessageReaction reaction) {

        this.listener = listener;
        this.message  = message;
        this.user     = user;
        this.reaction = reaction;
    }

    /**
     * Retrieve the {@link IReactionListener} to which this {@link IReactionAction} is associated.
     *
     * @return An {@link IReactionListener} implementation.
     */
    @Override
    public IReactionListener getListener() {

        return this.listener;
    }

    /**
     * Retrieve the {@link Message} to which this {@link IReactionAction} is associated.
     *
     * @return The {@link Message}.
     */
    @Override
    public Message getMessage() {

        return this.message;
    }

    /**
     * Retrieve the {@link User} to which this {@link IReactionAction} is associated.
     *
     * @return The {@link User}.
     */
    @Override
    public User getUser() {

        return this.user;
    }

    /**
     * Retrieve the {@link MessageReaction} that triggered this {@link IReactionAction}.
     *
     * @return The {@link MessageReaction}.
     */
    @Override
    public MessageReaction getReaction() {

        return this.reaction;
    }

    /**
     * Tries to remove the {@link MessageReaction} from the {@link Message}.
     * <p>
     * NOTE: This require the {@link Permission#MESSAGE_MANAGE} permission.
     */
    @Override
    public void removeReaction() {

        this.reaction.removeReaction(this.user).queue();
    }
}
