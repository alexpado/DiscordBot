package fr.alexpado.jda.interfaces;

import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import fr.alexpado.jda.services.commands.interfaces.ICommandHelper;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface representing a Discord Bot.
 *
 * @author alexpado
 */
public interface IDiscordBot {

    /**
     * Initiate the login sequence to Discord.
     *
     * @param token
     *         The token to use when login in to Discord
     */
    void login(@NotNull String token);

    /**
     * Retrieve the {@link ICommandHandler} in use for this {@link IDiscordBot}.
     *
     * @return An {@link ICommandHandler} implementation.
     */
    @NotNull ICommandHandler getCommandHandler();

    /**
     * Retrieve the {@link ICommandHelper} that will be used by this {@link IDiscordBot}'s {@link ICommandHandler}.
     *
     * @return An {@link ICommandHelper} implementation.
     */
    @NotNull ICommandHelper getCommandHelper();

    /**
     * Retrieve a list of {@link ListenerAdapter} to register before the {@link IDiscordBot} login.
     *
     * @return A list of {@link ListenerAdapter}.
     */
    @NotNull List<ListenerAdapter> getListenerAdapters();

}
