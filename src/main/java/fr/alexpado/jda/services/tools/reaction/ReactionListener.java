package fr.alexpado.jda.services.tools.reaction;


import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import fr.alexpado.jda.services.tools.interfaces.IReactionAction;
import fr.alexpado.jda.services.tools.interfaces.IReactionListener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ReactionListener extends ListenerAdapter implements IReactionListener {

    private final TimerTask                                  editorTimer;
    private final Timer                                      timer           = new Timer();
    private final HashMap<String, Consumer<IReactionAction>> reactionActions = new HashMap<>();
    private       Message                                    message;
    private       boolean                                    resetTimer      = false;

    public ReactionListener(Message message, int timeout) {

        this.message     = message;
        this.editorTimer = new EditorTimer(timeout);
    }

    /**
     * Associate the provided emote to the specified {@link IReactionAction} consumer.
     *
     * @param emote
     *         The emote to associate
     * @param action
     *         The action to execute when a reaction uses the emote.
     */
    @Override
    public void addAction(@NotNull String emote, @NotNull Consumer<IReactionAction> action) {

        this.message.addReaction(emote).queue();
        this.reactionActions.put(emote, action);
    }

    /**
     * Dissociate the provided emote from this listener and the message.
     *
     * @param emote
     *         The emote to dissociate.
     */
    @Override
    public void removeAction(@NotNull String emote) {

        if (this.getMessage() != null) {
            this.getMessage().removeReaction(emote).queue();
        }
        this.reactionActions.remove(emote);
    }

    /**
     * Reset the internal timer to its initial value for this {@link IReactionListener}.
     */
    @Override
    public void resetTimer() {

        this.resetTimer = true;
    }

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
    @Override
    public @Nullable Message getMessage() {

        return this.message;
    }

    /**
     * Define the {@link Message} on which this {@link IReactionListener} should listen for incoming {@link
     * MessageReactionAddEvent}.
     *
     * @param message
     *         The {@link Message}.
     */
    @Override
    public void setMessage(@NotNull Message message) {

        this.message = message;
    }

    /**
     * Start the internal timer for this listener and start listening for incoming {@link MessageReactionAddEvent}
     * events.
     *
     * @throws IllegalStateException
     *         Threw if {@link #getMessage()} is <code>null</code>.
     */
    @Override
    public void start() {

        if (this.getMessage() == null) {
            throw new IllegalStateException("No message has been defined.");
        }

        this.message.getJDA().addEventListener(this);
        this.timer.scheduleAtFixedRate(this.editorTimer, 0, 1000);
    }

    /**
     * Cancel the internal timer and immediately stop this {@link IReactionListener}. This is also used by the internal
     * timer when the timeout is reached.
     */
    @Override
    public void timeout() {

        this.timer.cancel();

        if (this.getMessage() != null) {
            this.getMessage().clearReactions().queue();
            this.getMessage().getJDA().removeEventListener(this);
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        if (event.getMessageIdLong() != this.message.getIdLong()) {
            return;
        }
        String emote = event.getReactionEmote().getName();
        if (Objects.equals(event.getUser(), event.getJDA().getSelfUser())) {
            return;
        }
        IReactionAction           action   = new ReactionAction(this, this.message, event.getUser(), event.getReaction());
        Consumer<IReactionAction> consumer = this.reactionActions.get(emote);

        if (consumer != null) {
            consumer.accept(action);
        }
    }

    private class EditorTimer extends TimerTask {

        private final int timeout;
        private       int timeLeft;

        EditorTimer(int timeout) {

            this.timeout  = timeout;
            this.timeLeft = timeout;
        }


        @Override
        public void run() {

            if (ReactionListener.this.resetTimer) {
                this.timeLeft                    = this.timeout;
                ReactionListener.this.resetTimer = false;
            } else if (this.timeLeft == 0) {
                ReactionListener.this.timeout();
            } else {
                this.timeLeft--;
            }

        }

    }

}
