package fr.alexpado.jda.services.commands.interfaces;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * Interface representing a {@link ICommand} being executed by an {@link User}.
 *
 * @author alexpado
 */
public interface ICommandEvent {

    /**
     * Check if this event has been cancelled.
     *
     * @return True if this event should be cancelled, false instead.
     */
    boolean isCancelled();

    /**
     * Define if this event should be cancelled.
     *
     * @param cancelled
     *         True if this event should be cancelled, false instead.
     */
    void setCancelled(boolean cancelled);

    /**
     * Get the label that has triggered this {@link ICommandEvent}.
     *
     * @return The {@link ICommand} label.
     */
    String getLabel();

    /**
     * Get the {@link ICommand} that will be executed is {@link #isCancelled()} returns {@code false}.
     *
     * @return An {@link ICommand} implementation.
     */
    ICommand getCommand();

    /**
     * Retrieve the {@link GuildMessageReceivedEvent} that have created this event.
     *
     * @return The {@link JDA} {@link GuildMessageReceivedEvent}.
     */
    GuildMessageReceivedEvent getEvent();

}
