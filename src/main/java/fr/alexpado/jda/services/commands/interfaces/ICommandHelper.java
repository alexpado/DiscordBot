package fr.alexpado.jda.services.commands.interfaces;

import fr.alexpado.jda.interfaces.IDiscordBot;
import fr.alexpado.jda.services.commands.annotations.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public interface ICommandHelper {

    /**
     * Called by {@link ICommandHandler}.
     * <p>
     * Creates a new instance implementing {@link ICommandContext}. This will be then used by all commands that require
     * a command context.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent} needing an {@link ICommandContext}.
     *
     * @return The {@link ICommandContext} implementation.
     */
    ICommandContext createContext(GuildMessageReceivedEvent event);

    /**
     * Retrieve the {@link EmbedBuilder} that may be used by {@link ICommand} to provide the same base across all your
     * bot.
     * <p>
     * This is particularly useful when you want to add a title or footer to all {@link MessageEmbed}.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent} to use to generate the {@link EmbedBuilder}.
     *
     * @return A {@link EmbedBuilder} template.
     */
    EmbedBuilder getVendorEmbed(@NotNull GuildMessageReceivedEvent event);

    /**
     * Retrieve the {@link EmbedBuilder} that may be used by {@link ICommand} to provide the same base across all your
     * bot.
     * <p>
     * This is particularly useful when you want to add a title or footer to all {@link MessageEmbed}.
     *
     * @param jda
     *         The {@link JDA} to use to generate the {@link EmbedBuilder}.
     *
     * @return A {@link EmbedBuilder} template.
     */
    EmbedBuilder getVendorEmbed(@NotNull JDA jda);

    /**
     * Called by {@link ICommandHandler}.
     * <p>
     * Retrieve the command prefix to use with the provided {@link GuildMessageReceivedEvent}. To use the configured
     * default prefix, you may want to return <code>null</code>.
     * <p>
     * This method being called doesn't assure that the {@link GuildMessageReceivedEvent} is associated with a command,
     * but will rather depends on the prefix provided.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent}.
     *
     * @return The prefix to use to check if the {@link GuildMessageReceivedEvent} can be handled as command.
     */
    String getApplicablePrefix(@NotNull GuildMessageReceivedEvent event);

    /**
     * Called by {@link ICommandHandler}.
     * <p>
     * Retrieve the message to send to the user if his message started with the command prefix, but not {@link ICommand}
     * could be matched.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent} that caused this command.
     * @param label
     *         The label of the command that wasn't found.
     *
     * @return The {@link EmbedBuilder} to send to the user.
     */
    EmbedBuilder onCommandNotFound(@NotNull GuildMessageReceivedEvent event, @NotNull String label);

    /**
     * Called by {@link ICommandHandler}.
     * <p>
     * Retrieve the message to send to the user if his message could be matched with an {@link ICommand} but no method
     * annotated with {@link Command} could be matched.
     *
     * @param command
     *         The {@link ICommand} that was in use.
     * @param event
     *         The {@link GuildMessageReceivedEvent} that caused this command.
     *
     * @return The {@link EmbedBuilder} to send to the user.
     */
    EmbedBuilder onSyntaxError(@NotNull ICommand command, @NotNull GuildMessageReceivedEvent event);

    /**
     * Called by {@link ICommandHandler}.
     * <p>
     * Retrieve the message to send to the user if a command encountered an error.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent} that caused this command.
     * @param throwable
     *         The {@link Throwable} that was thrown.
     *
     * @return The {@link EmbedBuilder} to send to the user.
     */
    EmbedBuilder onException(@NotNull GuildMessageReceivedEvent event, @NotNull Throwable throwable);

    /**
     * Called by {@link ICommandHandler}.
     * <p>
     * This method is called when the {@link IDiscordBot} doesn't have the permission to send a {@link Message} or an
     * {@link EmbedBuilder} in a channel.
     * <p>
     * This will happen after {@link #onCommandExecuted(ICommandEvent)}.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent} that caused this command.
     */
    void onPermissionMissing(@NotNull GuildMessageReceivedEvent event);

    /**
     * Called by {@link ICommandHandler}.
     * <p>
     * This method is called right before an {@link ICommand} execution. You may want to use this to log executed
     * command for statistics purpose, or cancel a command based on {@link ICommandEvent} content.
     *
     * @param event
     *         The {@link ICommandEvent} containing information about the current command flow.
     */
    void onCommandExecuted(@NotNull ICommandEvent event);

}
