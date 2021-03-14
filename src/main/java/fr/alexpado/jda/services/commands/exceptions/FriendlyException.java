package fr.alexpado.jda.services.commands.exceptions;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class FriendlyException extends RuntimeException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message
     *         the detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public FriendlyException(String message) {

        super(message);
    }

    public EmbedBuilder toEmbed() {

        return new EmbedBuilder().setColor(Color.RED).setDescription(this.getMessage());
    }
}
