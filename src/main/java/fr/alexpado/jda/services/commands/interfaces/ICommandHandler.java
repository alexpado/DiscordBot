package fr.alexpado.jda.services.commands.interfaces;

import fr.alexpado.jda.interfaces.IDiscordContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

/**
 * Interface representing an {@link ICommand} handler.
 *
 * @author alexpado
 */
public interface ICommandHandler extends IDiscordContext {

    /**
     * Retrieve an {@link Optional} {@link ICommand} matching the provided label.
     *
     * @param label
     *         The label associated to the {@link ICommand} to retrieve
     *
     * @return An {@link Optional} {@link ICommand}
     */
    @NotNull Optional<ICommand> getCommand(@NotNull String label);

    /**
     * Register the provided command.
     *
     * @param command
     *         The {@link ICommand} to register.
     */
    void register(@NotNull ICommand command);

    /**
     * Get a {@link Map} mapping every registered command label and their corresponding {@link ICommand}.
     *
     * @return A {@link Map} of {@link String} and {@link ICommand}
     */
    @NotNull Map<String, ICommand> getCommands();

    /**
     * Handle the command execution from the provided {@link GuildMessageReceivedEvent}.
     *
     * @param event
     *         The {@link JDA}'s {@link GuildMessageReceivedEvent}.
     */
    void handle(@NotNull GuildMessageReceivedEvent event);

    /**
     * Pre-handling stage with the {@link GuildMessageReceivedEvent} directly called by {@link JDA}. Here, you may want
     * to check if the message match your command pattern before calling {@link #handle(GuildMessageReceivedEvent)}.
     *
     * @param event
     *         The {@link JDA}'s {@link GuildMessageReceivedEvent}.
     */
    void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event);

    /**
     * Retrieve an optional message that will be display above all {@link ICommand} results. This may be useful to share
     * important or critical information to your bot's users.
     *
     * @return An option message that will be displayed above all {@link ICommand} results.
     */
    Optional<String> getNotice();

    /**
     * Define the message to display above of all bot's messages. Provide <code>null</code> to remove the notice.
     *
     * @param message
     *         The message to display.
     */
    void setNotice(@Nullable String message);

}
