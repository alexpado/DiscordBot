package fr.alexpado.jda;

import fr.alexpado.jda.interfaces.IDiscordBot;
import fr.alexpado.jda.services.commands.JDACommandHandler;
import fr.alexpado.jda.services.commands.interfaces.ICommand;
import fr.alexpado.jda.services.commands.interfaces.ICommandEvent;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Collection;

/**
 * Class implementing {@link IDiscordBot}.
 * <p>
 * This is the main class when creating a Discord bot. You have to extend it.
 *
 * @author alexpado
 */
public abstract class DiscordBotImpl extends ListenerAdapter implements IDiscordBot {

    private static final Logger          LOGGER = LoggerFactory.getLogger(DiscordBotImpl.class);
    private final        ICommandHandler commandHandler;
    private final        JDABuilder      jdaBuilder;

    /**
     * Create a new instance of {@link DiscordBotImpl} using the provided {@link Collection} of {@link GatewayIntent}
     * and the provided prefix for the {@link ICommandHandler}.
     *
     * @param intents
     *         A {@link Collection} of {@link GatewayIntent} to use.
     * @param prefix
     *         The prefix to use when creating this {@link DiscordBotImpl}'s {@link ICommandHandler}.
     */
    protected DiscordBotImpl(@NotNull Collection<GatewayIntent> intents, @NotNull String prefix) {

        this.jdaBuilder = JDABuilder.create(intents);
        this.jdaBuilder.addEventListeners(this);

        this.commandHandler = new JDACommandHandler(this, prefix);
        this.jdaBuilder.addEventListeners(this.commandHandler);
    }

    /**
     * Create a new instance of {@link DiscordBotImpl} using {@link GatewayIntent#DEFAULT} intents and the provided
     * prefix for the {@link ICommandHandler}.
     *
     * @param prefix
     *         The prefix to use when creating this {@link DiscordBotImpl}'s {@link ICommandHandler}
     */
    protected DiscordBotImpl(@NotNull String prefix) {

        this(GatewayIntent.getIntents(GatewayIntent.DEFAULT), prefix);
    }


    /**
     * Initiate the login sequence to Discord.
     *
     * @param token
     *         The token to use when login in to Discord
     */
    @Override
    public final void login(String token) {

        try {
            this.jdaBuilder.setToken(token);
            this.jdaBuilder.build();
        } catch (LoginException e) {
            LOGGER.warn("Unable to connect to Discord. The token provided is probably invalid.", e);
        }
    }

    @Override
    public ICommandHandler getCommandHandler() {

        return this.commandHandler;
    }

    /**
     * Called by {@link ICommandHandler} when an {@link ICommand} matching the provided label couldn't be found.
     *
     * @param event
     *         The {@link JDA} {@link GuildMessageReceivedEvent}.
     * @param label
     *         The label that has been used to try matching an {@link ICommand}
     */
    @Override
    public void onCommandNotFound(@NotNull GuildMessageReceivedEvent event, @NotNull String label) {

        LOGGER.debug("User {} tried to execute the command {}.", event.getAuthor().getIdLong(), label);
    }

    /**
     * Called by {@link ICommandHandler}
     *
     * @param event
     *         The {@link ICommandEvent} triggering this event.
     */
    @Override
    public void onCommandExecuted(@NotNull ICommandEvent event) {

        LOGGER.debug("The {} command will be executed by user {}.", event.getLabel(), event.getEvent()
                                                                                           .getAuthor()
                                                                                           .getIdLong());
    }
}
