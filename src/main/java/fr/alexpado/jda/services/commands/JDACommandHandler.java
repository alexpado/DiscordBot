package fr.alexpado.jda.services.commands;

import fr.alexpado.jda.interfaces.IDiscordBot;
import fr.alexpado.jda.services.commands.impl.CommandEventImpl;
import fr.alexpado.jda.services.commands.interfaces.ICommand;
import fr.alexpado.jda.services.commands.interfaces.ICommandEvent;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class implementing {@link ICommandHandler}.
 * <p>
 * This class is used for managing every {@link ICommand} registered by {@link IDiscordBot}.
 *
 * @author alexpado
 */
public class JDACommandHandler extends ListenerAdapter implements ICommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(JDACommandHandler.class);

    private final IDiscordBot           discordBot;
    private final Map<String, ICommand> commandMap;
    private final String                prefix;

    /**
     * Create a new instance of this {@link ICommandHandler} implementation.
     *
     * @param discordBot
     *         The {@link IDiscordBot} which is creating this {@link ICommandHandler} implementation.
     * @param prefix
     *         The prefix to use for this {@link ICommandHandler}
     *
     * @throws IllegalArgumentException
     *         Threw if the provided prefix is empty.
     */
    public JDACommandHandler(@NotNull IDiscordBot discordBot, @NotNull String prefix) {

        this.discordBot = discordBot;
        this.commandMap = new HashMap<>();
        this.prefix     = prefix;

        if (this.prefix.isEmpty()) {
            logger.warn("Invalid prefix used: Empty prefix shouldn't be used, as it would match every single message.");
            throw new IllegalArgumentException("Unable to create CommandHandler: Invalid prefix.");
        }

        logger.info("A new command handler has been created with the prefix {}.", this.prefix);
    }

    /**
     * Retrieve an {@link Optional} {@link ICommand} matching the provided label.
     *
     * @param label
     *         The label associated to the {@link ICommand} to retrieve
     *
     * @return An {@link Optional} {@link ICommand}
     */
    @Override
    @NotNull
    public Optional<ICommand> getCommand(@NotNull String label) {

        return Optional.ofNullable(this.commandMap.get(label));
    }

    /**
     * Register the provided command under the provided label.
     *
     * @param label
     *         The label associated to the {@link ICommand} to retrieve.
     * @param command
     *         The {@link ICommand} to register.
     *
     * @throws IllegalArgumentException
     *         Threw is the provided label is already registered.
     */
    @Override
    public void register(@NotNull String label, @NotNull ICommand command) {

        if (this.getCommand(label).isPresent()) {
            logger.warn("A module tried to register the command '{}' which is already registered.", label);
            throw new IllegalArgumentException(String.format("The `%s` is already registered.", label));
        }
        this.commandMap.put(label, command);
    }

    /**
     * Get a {@link Map} mapping every registered command label and their corresponding {@link ICommand}.
     *
     * @return A {@link Map} of {@link String} and {@link ICommand}
     */
    @Override
    @NotNull
    public Map<String, ICommand> getCommands() {

        return this.commandMap;
    }

    /**
     * Handle the command execution from the provided {@link GuildMessageReceivedEvent}
     *
     * @param event
     *         The {@link JDA}'s {@link GuildMessageReceivedEvent}.
     */
    @Override
    public void handle(@NotNull GuildMessageReceivedEvent event) {

        logger.debug("handle() called. Processing command.");
        String message = event.getMessage().getContentRaw().toLowerCase();
        String label   = message.split(" ")[0].replace(this.prefix.toLowerCase(), "");
        logger.debug("Detected '{}' label.", label);

        Optional<ICommand> optionalCommand = this.getCommand(label);

        if (!optionalCommand.isPresent()) {
            this.discordBot.onCommandNotFound(event, label);
            return;
        }

        ICommand      command      = optionalCommand.get();
        ICommandEvent commandEvent = new CommandEventImpl(event, label, command);

        this.discordBot.onCommandExecuted(commandEvent);

        if (!commandEvent.isCancelled()) {
            command.execute(event);
            logger.debug("The command '{}' has been successfully executed.", label);
            return;
        }

        logger.debug("Command execution cancelled.");
    }

    /**
     * Called when {@link JDA} catch a {@link Message} being sent in a {@link Guild}.
     *
     * @param event
     *         The {@link JDA}'s {@link GuildMessageReceivedEvent}
     */
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw().toLowerCase();
        if (message.startsWith(this.prefix.toLowerCase())) {
            this.handle(event);
        }
    }
}
