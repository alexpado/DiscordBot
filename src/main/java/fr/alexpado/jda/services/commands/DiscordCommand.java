package fr.alexpado.jda.services.commands;

import fr.alexpado.jda.interfaces.IDiscordBot;
import fr.alexpado.jda.services.commands.annotations.Command;
import fr.alexpado.jda.services.commands.exceptions.SyntaxException;
import fr.alexpado.jda.services.commands.interfaces.ICommand;
import fr.alexpado.jda.services.commands.interfaces.ICommandContext;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import fr.alexpado.jda.services.syntax.SyntaxService;
import fr.alexpado.jda.services.syntax.SyntaxUtils;
import fr.alexpado.jda.services.syntax.interfaces.IMatchingResult;
import fr.alexpado.jda.services.syntax.interfaces.ISyntax;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxContainer;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxService;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class implementing {@link ICommand}.
 * <p>
 * This is the main class to use when creating a bot command. Every public method being annotated with {@link Command}
 * will be candidates for being a command.
 *
 * @author alexpado
 */
public abstract class DiscordCommand implements ICommand {

    private final IDiscordBot                   bot;
    private final Map<Method, ISyntaxContainer> syntaxMap;

    /**
     * Creates a new {@link DiscordCommand} instance and register it immediately.
     *
     * @param discordBot
     *         The {@link IDiscordBot} associated with this command.
     */
    protected DiscordCommand(IDiscordBot discordBot) {

        this.bot       = discordBot;
        this.syntaxMap = this.getSyntaxMap();
    }

    /**
     * Retrieves the {@link IDiscordBot} from which this {@link ICommand} has been register.
     *
     * @return An {@link IDiscordBot} implementation.
     */
    @Override
    @NotNull
    public final IDiscordBot getBot() {

        return this.bot;
    }

    /**
     * Called by the {@link ICommandHandler} when an {@link User} execute this command.
     *
     * @param context
     *         The {@link GuildMessageReceivedEvent} for the current command's execution flow.
     *
     * @throws Exception
     *         Thrown if something happen during the command's execution.
     */
    @Override
    public Object execute(@NotNull ICommandContext context) throws Exception {

        String message = context.getEvent().getMessage().getContentRaw();
        List<String> userInput = Arrays.stream(message.trim().split(" "))
                                       .filter(s -> !s.isEmpty())
                                       .collect(Collectors.toList());

        IMatchingResult<Method> matchingResult = this.getMatch(String.join(" ", userInput.subList(1, userInput.size())));
        Method                  exec           = matchingResult.getIdentifier();
        List<Object>            parameters     = CommandUtils.createParameters(matchingResult, context);

        return exec.invoke(this, parameters.toArray());
    }

    /**
     * Retrieve the {@link Map} associating an {@link ISyntax} to its possible values.
     *
     * @return A {@link Map}
     */
    @Override
    public @NotNull Map<String, List<String>> getOptions() {

        return new HashMap<>();
    }

    private ISyntaxService<Method> getCompletionService() {

        return new SyntaxService<>(this.syntaxMap);
    }

    private Map<Method, ISyntaxContainer> getSyntaxMap() {

        Map<Method, ISyntaxContainer> map     = new HashMap<>();
        Map<String, List<String>>     options = this.getOptions();
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            Command command = declaredMethod.getAnnotation(Command.class);

            if (command != null) {
                map.put(declaredMethod, SyntaxUtils.toContainer(options, command.value(), command.order()));
            }
        }

        return map;
    }

    private IMatchingResult<Method> getMatch(String userInput) {

        return this.getCompletionService()
                   .getMatchingResult(userInput)
                   .orElseThrow(() -> new SyntaxException("Command not found. Please check your syntax."));
    }
}
