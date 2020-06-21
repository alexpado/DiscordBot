package fr.alexpado.jda.services.commands.impl;

import fr.alexpado.jda.services.commands.interfaces.ICommand;
import fr.alexpado.jda.services.commands.interfaces.ICommandEvent;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * Class implementing the {@link ICommandEvent}. This will be used by {@link ICommandHandler} to provide a way to the
 * bot developer to cancel a command execution if necessary (ex: A command reserved only for the developer).
 *
 * @author alexpado
 */
public class CommandEventImpl implements ICommandEvent {

    private final GuildMessageReceivedEvent event;
    private final String                    label;
    private final ICommand                  command;
    private       boolean                   cancelled = false;

    /**
     * Creates a new {@link CommandEventImpl} instance.
     *
     * @param event
     *         The {@link JDA} {@link GuildMessageReceivedEvent}.
     * @param label
     *         The label of the {@link ICommand} being executed.
     * @param command
     *         The {@link ICommand} being executed.
     */
    public CommandEventImpl(GuildMessageReceivedEvent event, String label, ICommand command) {

        this.event   = event;
        this.label   = label;
        this.command = command;
    }

    /**
     * Check if this event has been cancelled.
     *
     * @return True if this event should be cancelled, false instead.
     */
    @Override
    public boolean isCancelled() {

        return this.cancelled;
    }

    /**
     * Define if this event should be cancelled.
     *
     * @param cancelled
     *         True if this event should be cancelled, false instead.
     */
    @Override
    public void setCancelled(boolean cancelled) {

        this.cancelled = cancelled;
    }

    /**
     * Get the label that has triggered this {@link ICommandEvent}.
     *
     * @return The {@link ICommand} label.
     */
    @Override
    public String getLabel() {

        return this.label;
    }

    /**
     * Get the {@link ICommand} that will be executed is {@link #isCancelled()} returns {@code false}.
     *
     * @return An {@link ICommand} implementation.
     */
    @Override
    public ICommand getCommand() {

        return this.command;
    }

    /**
     * Retrieve the {@link GuildMessageReceivedEvent} that have created this event.
     *
     * @return The {@link JDA} {@link GuildMessageReceivedEvent}.
     */
    @Override
    public GuildMessageReceivedEvent getEvent() {

        return this.event;
    }
}
