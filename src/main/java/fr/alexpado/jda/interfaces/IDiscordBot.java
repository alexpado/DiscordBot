package fr.alexpado.jda.interfaces;

import fr.alexpado.jda.services.commands.interfaces.ICommand;
import fr.alexpado.jda.services.commands.interfaces.ICommandEvent;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Interface representing a Discord Bot.
 *
 * @author alexpado
 */
public interface IDiscordBot {

    /**
     * Called by {@link ICommandHandler} when an {@link ICommand} matching the provided label couldn't be found.
     *
     * @param event
     *         The {@link JDA} {@link GuildMessageReceivedEvent}.
     * @param label
     *         The label that has been used to try matching an {@link ICommand}
     */
    void onCommandNotFound(@NotNull GuildMessageReceivedEvent event, @NotNull String label);

    /**
     * Called by {@link ICommandHandler} when a command is executed. You may use this method to intercept a {@link
     * ICommandEvent} and cancelling it.
     *
     * @param event
     *         The {@link ICommandEvent} triggering this event.
     */
    void onCommandExecuted(@NotNull ICommandEvent event);

    /**
     * Initiate the login sequence to Discord.
     *
     * @param token
     *         The token to use when login in to Discord
     */
    void login(String token);

    /**
     * Retrieve the {@link ICommandHandler} in use for this {@link IDiscordBot}.
     *
     * @return An {@link ICommandHandler} implementation.
     */
    ICommandHandler getCommandHandler();

}
