package fr.alexpado.jda.services.reactions;


import fr.alexpado.jda.services.reactions.interfaces.IReactionAction;
import fr.alexpado.jda.services.reactions.interfaces.IReactionListener;
import fr.alexpado.jda.services.reactions.interfaces.IReactionTimer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ReactionListener extends ListenerAdapter implements IReactionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactionListener.class);

    private final Timer          timer = new Timer();
    private final IReactionTimer reactionTimer;
    private final Message        message;

    private final HashMap<String, Consumer<IReactionAction>> reactionActions = new HashMap<>();

    public ReactionListener(Message message, int timeout) {

        this.message       = message;
        this.reactionTimer = new ReactionTimer(this, timeout);
    }

    /**
     * Retrieve the {@link Message} on which this {@link IReactionListener} is active.
     *
     * @return A {@link Message} instance.
     */
    @Override
    public Message getMessage() {

        return this.message;
    }

    /**
     * Add an action to this {@link IReactionListener}.
     *
     * @param emote
     *         The emote that will trigger the action.
     * @param action
     *         The action to execute.
     */
    @Override
    public void addAction(String emote, Consumer<IReactionAction> action) {

        this.reactionActions.put(emote, action);
        this.message.addReaction(emote).queue();
    }

    /**
     * Register this {@link IReactionListener} and start the timeout timer.
     */
    @Override
    public void start() {

        this.timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                ReactionListener.this.reactionTimer.run();
            }
        }, 0, 1000);

        this.message.getJDA().addEventListener(this);

        LOGGER.debug("Started new ReactionLister on message {}.", this.message.getId());
    }

    /**
     * Unregister this {@link IReactionListener} and stop the timeout timer.
     */
    @Override
    public void stop() {

        this.message.getJDA().removeEventListener(this);
        this.timer.cancel();
        this.message.clearReactions().queue();
        LOGGER.debug("Stopped ReactionLister on message {}.", this.message.getId());
    }

    /**
     * Reset the timeout value of this {@link IReactionListener} to the one defined in the constructor.
     */
    @Override
    public void resetTimer() {

        this.reactionTimer.reset();
    }


    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        if (event.getMessageIdLong() != this.message.getIdLong()) {
            return; // Ignore different message.
        }

        if (Objects.equals(event.getUser(), event.getJDA().getSelfUser())) {
            return; // Ignore self-user.
        }

        String emote = event.getReactionEmote().getName();

        IReactionAction           action   = new ReactionAction(this, event.getUser(), event.getReaction());
        Consumer<IReactionAction> consumer = this.reactionActions.get(emote);
        if (consumer != null) {
            consumer.accept(action);
        } else {
            LOGGER.debug("User {} added an unknown emote action.", event.getUserIdLong());
        }
    }

}
