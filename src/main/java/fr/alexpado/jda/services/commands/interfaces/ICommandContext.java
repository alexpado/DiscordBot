package fr.alexpado.jda.services.commands.interfaces;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface ICommandContext {

    /**
     * Retrieves the {@link GuildMessageReceivedEvent} that caused this {@link ICommandContext} to be created.
     *
     * @return A {@link GuildMessageReceivedEvent}.
     */
    GuildMessageReceivedEvent getEvent();

}
