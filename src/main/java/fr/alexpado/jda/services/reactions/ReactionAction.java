package fr.alexpado.jda.services.reactions;


import fr.alexpado.jda.services.reactions.interfaces.IReactionAction;
import fr.alexpado.jda.services.reactions.interfaces.IReactionListener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

public class ReactionAction implements IReactionAction {

    private final IReactionListener listener;
    private final User              user;
    private final MessageReaction   reaction;

    ReactionAction(IReactionListener listener, User user, MessageReaction reaction) {

        this.listener = listener;
        this.user     = user;
        this.reaction = reaction;
    }

    /**
     * Retrieve the {@link IReactionListener} that trigger this {@link IReactionAction}.
     *
     * @return An {@link IReactionListener} instance.
     */
    @Override
    public @NotNull IReactionListener getListener() {

        return this.listener;
    }

    /**
     * Retrieve the {@link Message} on which this {@link IReactionAction} has been triggered.
     *
     * @return A {@link Message} instance.
     */
    @Override
    public @NotNull Message getMessage() {

        return this.getListener().getMessage();
    }

    /**
     * Retrieve the {@link User} that triggered this {@link IReactionAction}.
     *
     * @return An {@link User} instance.
     */
    @Override
    public @NotNull User getUser() {

        return this.user;
    }

    /**
     * Retrieve the {@link MessageReaction} instance containing all information about the reaction.
     *
     * @return A {@link MessageReaction}.
     */
    @Override
    public @NotNull MessageReaction getReaction() {

        return this.reaction;
    }

    /**
     * Remove the reaction from the message. This will affect all reactions added by the {@link User}.
     *
     * @return A {@link RestAction} instance.
     *
     * @see MessageReaction#removeReaction(User).
     */
    @Override
    public @NotNull RestAction<Void> removeReaction() {

        return this.getReaction().removeReaction(this.getUser());
    }
}
