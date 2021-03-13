package fr.alexpado.jda.services.commands;

import fr.alexpado.jda.interfaces.IDiscordBot;
import fr.alexpado.jda.services.commands.exceptions.SyntaxException;
import fr.alexpado.jda.services.commands.impl.CommandEventImpl;
import fr.alexpado.jda.services.commands.interfaces.ICommand;
import fr.alexpado.jda.services.commands.interfaces.ICommandContext;
import fr.alexpado.jda.services.commands.interfaces.ICommandEvent;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import fr.alexpado.jda.services.tools.embed.EmbedPage;
import io.sentry.Sentry;
import io.sentry.protocol.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Class implementing {@link ICommandHandler}.
 * <p>
 * This class is used for managing every {@link ICommand} registered by {@link IDiscordBot}.
 *
 * @author alexpado
 */
public class JDACommandHandler extends ListenerAdapter implements ICommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDACommandHandler.class);
    private static final List<Permission> REQUIRED_PERMISSIONS = Arrays.asList(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS);
    private static final List<Permission> OPTIONAL_PERMISSIONS = Arrays.asList(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_MANAGE);

    private final Map<String, ICommand> commandMap;

    private final IDiscordBot bot;
    private       String      notice;

    /**
     * Create a new instance of this {@link ICommandHandler} implementation.
     *
     * @param bot
     *         The {@link IDiscordBot} which is creating this {@link ICommandHandler} implementation.
     *
     * @throws IllegalArgumentException
     *         Threw if the provided prefix is empty.
     */
    public JDACommandHandler(@NotNull IDiscordBot bot) {

        this.bot        = bot;
        this.commandMap = new HashMap<>();

        LOGGER.info("A new command handler has been created.");
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
     * @param command
     *         The {@link ICommand} to register.
     *
     * @throws IllegalArgumentException
     *         Threw is the provided label is already registered.
     */
    @Override
    public void register(@NotNull ICommand command) {

        String label = command.getMeta().getLabel();

        if (this.getCommand(label).isPresent()) {
            LOGGER.warn("A module tried to register the command '{}' which is already registered.", label);
            throw new IllegalArgumentException(String.format("The `%s` command is already registered.", label));
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

        String prefix = this.bot.getCommandHelper().getApplicablePrefix(event);
        String msg    = event.getMessage().getContentRaw().toLowerCase();
        String label  = msg.split(" ")[0].replace(prefix.toLowerCase(), "");

        Optional<ICommand> optionalCommand = this.getCommand(label);

        if (optionalCommand.isEmpty()) {
            EmbedBuilder message = this.bot.getCommandHelper().onCommandNotFound(event, label);
            event.getChannel().sendMessage(message.build()).queue();
            return;
        }

        ICommand command = optionalCommand.get();

        // Build command context
        ICommandEvent commandEvent = new CommandEventImpl(event, command);
        this.bot.getCommandHelper().onCommandExecuted(commandEvent);

        if (!commandEvent.isCancelled()) {

            if (!event.getGuild().getSelfMember().hasPermission(event.getChannel(), REQUIRED_PERMISSIONS)) {
                this.getBot().getCommandHelper().onPermissionMissing(event);
                return;
            }

            try {
                ICommandContext context = this.getBot().getCommandHelper().createContext(event);
                Object          result  = command.execute(context);

                if (context instanceof AutoCloseable) {
                    ((AutoCloseable) context).close();
                }

                if (result instanceof MessageBuilder) {
                    MessageBuilder builder = (MessageBuilder) result;
                    event.getChannel().sendMessage(builder.build()).queue();
                } else if (result instanceof EmbedBuilder) {
                    EmbedBuilder   builder    = (EmbedBuilder) result;
                    this.send(event.getChannel(), builder);
                } else if (result instanceof EmbedPage) {
                    EmbedPage<?> page = (EmbedPage<?>) result;
                    event.getChannel()
                         .sendMessage(new EmbedBuilder().setDescription("...").build())
                         .queue(page::setMessage);
                } else if (result instanceof String) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setDescription(((CharSequence) result));
                    this.send(event.getChannel(), builder);
                }
            } catch (SyntaxException e) {
                EmbedBuilder   builder    = this.getBot().getCommandHelper().onSyntaxError(command, event);
                this.send(event.getChannel(), builder);
            } catch (Exception e) {
                Throwable reportableException;

                if (e instanceof InvocationTargetException) {
                    // If it's an invocation target exception, it means that the error was most likely within the command.
                    reportableException = e.getCause();
                } else {
                    reportableException = e;
                }

                this.report(event, label, reportableException);
                EmbedBuilder   builder    = this.bot.getCommandHelper().onException(event, reportableException);
                this.send(event.getChannel(), builder);
            }

        } else {
            LOGGER.debug("Command execution cancelled.");
        }
    }

    private void send(MessageChannel channel, EmbedBuilder builder) {
        MessageBuilder msgBuilder = new MessageBuilder();
        msgBuilder.setEmbed(builder.build());
        this.getNotice().ifPresent(msgBuilder::setContent);
        channel.sendMessage(msgBuilder.build()).queue();
    }

    private void report(GuildMessageReceivedEvent event, String label, Throwable throwable) {

        Sentry.withScope(scope -> {

            Map<String, String> context = new HashMap<>();

            context.put("guild", event.getGuild().getId());
            context.put("channel", event.getChannel().getId());
            context.put("user", event.getAuthor().getId());
            context.put("message", event.getMessageId());
            context.put("content", event.getMessage().getContentRaw());

            scope.setContexts("Discord", context);
            scope.setTag("command", label);

            User user = new User();

            user.setId(event.getAuthor().getId());
            user.setUsername(String.format("%s#%s", event.getAuthor().getName(), event.getAuthor().getDiscriminator()));

            scope.setUser(user);

            Sentry.captureException(throwable);
        });
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
        String prefix  = this.bot.getCommandHelper().getApplicablePrefix(event);

        if (message.startsWith(prefix.toLowerCase())) {
            this.handle(event);
        }
    }

    /**
     * Get the {@link IDiscordBot} associated with this {@link JDACommandHandler}.
     *
     * @return An {@link IDiscordBot} implementation instance.
     */
    @Override
    public IDiscordBot getBot() {

        return this.bot;
    }

    /**
     * Retrieve an optional message that will be display above all {@link ICommand} results. This may be useful to share
     * important or critical information to your bot's users.
     *
     * @return An option message that will be displayed above all {@link ICommand} results.
     */
    @Override
    public Optional<String> getNotice() {

        return Optional.ofNullable(this.notice);
    }

    /**
     * Define the message to display above of all bot's messages.
     *
     * @param message
     *         The message to display.
     */
    @Override
    public void setNotice(String message) {

        this.notice = message;
    }

}
