package fr.alexpado.jda.services.commands.interfaces;

import fr.alexpado.jda.interfaces.IDiscordBot;
import fr.alexpado.jda.interfaces.IDiscordContext;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface representing a command that can be run by any {@link User}.
 *
 * @author alexpado
 */
public interface ICommand extends IDiscordContext {

    /**
     * Retrieves a {@link MessageEmbed} containing all information about how to use this {@link ICommand}.
     *
     * @return A {@link MessageEmbed} instance, or null.
     */
    @Nullable MessageEmbed getHelp();

    /**
     * Execute this {@link ICommand} using data contained the provided {@link GuildMessageReceivedEvent}. If no
     * contained command matched, the {@link #getHelp()} method will be used.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent} to use.
     */
    void execute(@NotNull GuildMessageReceivedEvent event);

}
