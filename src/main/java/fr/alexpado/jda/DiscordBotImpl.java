package fr.alexpado.jda;

import fr.alexpado.jda.interfaces.IDiscordBot;
import fr.alexpado.jda.services.commands.JDACommandHandler;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import net.dv8tion.jda.api.JDABuilder;
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

    public static final String RAW_BOT_ADD_LINK = "https://discord.com/oauth2/authorize?client_id=%s&permissions=%s&scope=bot";

    private static final Logger          LOGGER = LoggerFactory.getLogger(DiscordBotImpl.class);
    private final        ICommandHandler commandHandler;
    private final        JDABuilder      jdaBuilder;

    /**
     * Create a new instance of {@link DiscordBotImpl} using the provided {@link Collection} of {@link GatewayIntent}.
     *
     * @param intents
     *         A {@link Collection} of {@link GatewayIntent} to use.
     */
    protected DiscordBotImpl(@NotNull Collection<GatewayIntent> intents) {

        this.jdaBuilder = JDABuilder.create(intents);
        this.jdaBuilder.addEventListeners(this);

        this.commandHandler = new JDACommandHandler(this);
        this.jdaBuilder.addEventListeners(this.commandHandler);
    }

    /**
     * Create a new instance of {@link DiscordBotImpl} using {@link GatewayIntent#DEFAULT} intents.
     */
    protected DiscordBotImpl() {

        this(GatewayIntent.getIntents(GatewayIntent.DEFAULT));
    }

    /**
     * Initiate the login sequence to Discord.
     *
     * @param token
     *         The token to use when login in to Discord
     */
    @Override
    public final void login(@NotNull String token) {

        try {
            this.jdaBuilder.setToken(token);
            this.getListenerAdapters().forEach(this.jdaBuilder::addEventListeners);
            this.jdaBuilder.build();
        } catch (LoginException e) {
            LOGGER.warn("Unable to connect to Discord. The token provided is probably invalid.", e);

            Sentry.withScope(scope -> {
                scope.setLevel(SentryLevel.FATAL);
                Sentry.captureException(e);
            });
        }
    }

    /**
     * Retrieve the {@link ICommandHandler} in use for this {@link IDiscordBot}.
     *
     * @return An {@link ICommandHandler} implementation.
     */
    @Override
    public @NotNull ICommandHandler getCommandHandler() {

        return this.commandHandler;
    }

}
