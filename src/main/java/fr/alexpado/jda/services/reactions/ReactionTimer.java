package fr.alexpado.jda.services.reactions;

import fr.alexpado.jda.services.reactions.interfaces.IReactionListener;
import fr.alexpado.jda.services.reactions.interfaces.IReactionTimer;

public class ReactionTimer implements IReactionTimer {

    private final IReactionListener listener;
    private final int               timeout;
    private       boolean           resetOnNextTick = false;
    private       int               timeLeft;

    ReactionTimer(IReactionListener listener, int timeout) {

        this.listener = listener;
        this.timeout  = timeout;
        this.timeLeft = timeout;
    }

    @Override
    public void reset() {

        this.resetOnNextTick = true;
    }


    @Override
    public void run() {

        if (this.resetOnNextTick) {

            this.timeLeft        = this.timeout;
            this.resetOnNextTick = false;
        }

        if (this.timeLeft == 0) {
            this.listener.stop();
        }

        this.timeLeft--;
    }

}
