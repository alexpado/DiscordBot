package fr.alexpado.jda.services.commands;

import fr.alexpado.jda.interfaces.IDiscordBot;
import fr.alexpado.jda.services.commands.annotations.Command;
import fr.alexpado.jda.services.commands.annotations.Param;
import fr.alexpado.jda.services.commands.exceptions.ExecutionException;
import fr.alexpado.jda.services.commands.exceptions.SyntaxException;
import fr.alexpado.jda.services.commands.interfaces.ICommand;
import fr.alexpado.jda.services.completion.CompletionServiceImpl;
import fr.alexpado.jda.services.completion.interfaces.ICompletionService;
import fr.alexpado.jda.services.completion.interfaces.IMatchingResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordCommand.class);

    private final IDiscordBot               bot;
    private final Map<Method, List<String>> syntaxMap;

    /**
     * Creates a new {@link DiscordCommand} instance.
     *
     * @param discordBot
     *         The {@link IDiscordBot} associated with this command.
     */
    protected DiscordCommand(IDiscordBot discordBot) {

        this.bot       = discordBot;
        this.syntaxMap = this.getSyntaxMap();
    }

    private static List<Object> generateParameterList(IMatchingResult<Method> matchingResult, Method method, GuildMessageReceivedEvent event) {

        Map<Class<?>, Object> paramTypeMap = new HashMap<>();

        paramTypeMap.put(User.class, event.getAuthor());
        paramTypeMap.put(Message.class, event.getMessage());
        paramTypeMap.put(Guild.class, event.getGuild());
        paramTypeMap.put(Member.class, event.getMember());
        paramTypeMap.put(TextChannel.class, event.getChannel());
        paramTypeMap.put(MessageChannel.class, event.getChannel());
        paramTypeMap.put(GuildChannel.class, event.getChannel());
        paramTypeMap.put(JDA.class, event.getJDA());
        paramTypeMap.put(GuildMessageReceivedEvent.class, event);

        List<Object> methodParameters = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Param.class)) {
                Param param = parameter.getAnnotation(Param.class);
                methodParameters.add(matchingResult.getParameter(param.value()));
            } else if (paramTypeMap.containsKey(parameter.getType())) {
                methodParameters.add(paramTypeMap.get(parameter.getType()));
            }
        }

        return methodParameters;
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
     * Retrieves a {@link MessageEmbed} containing all information about how to use this {@link ICommand}.
     *
     * @return A {@link MessageEmbed} instance, or null.
     */
    @Override
    public @Nullable MessageEmbed getHelp() {

        return null;
    }

    /**
     * Execute this {@link ICommand} using data contained the provided {@link GuildMessageReceivedEvent}. If no
     * contained command matched, the {@link #getHelp()} method will be used.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent} to use.
     */
    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event) {

        String       message   = event.getMessage().getContentRaw();
        List<String> userInput = Arrays.stream(message.trim().split(" "))
                                        .filter(s -> !s.isEmpty())
                                        .collect(Collectors.toList());

        LOGGER.info("Trying to match '{}'", userInput.subList(1, userInput.size()));

        IMatchingResult<Method> matchingResult = this.getMatch(String.join(" ", userInput.subList(1, userInput.size())));
        Method                  exec           = matchingResult.getIdentifier();
        List<Object>            parameters     = DiscordCommand.generateParameterList(matchingResult, exec, event);
        this.call(exec, parameters);
    }

    private ICompletionService<Method> getCompletionService() {

        return new CompletionServiceImpl<>(this.syntaxMap);
    }

    private Map<Method, List<String>> getSyntaxMap() {

        Map<Method, List<String>> map = new HashMap<>();

        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            Command command = declaredMethod.getAnnotation(Command.class);

            if (command != null) {
                map.put(declaredMethod, Arrays.asList(command.value().split(" ")));
            }
        }

        return map;
    }

    private IMatchingResult<Method> getMatch(String userInput) {

        return this.getCompletionService()
                   .getMatchingIdentifier(userInput)
                   .orElseThrow(() -> new SyntaxException("Command not found. Please check your syntax."));
    }

    private void call(Method method, Collection<Object> values) {

        try {
            method.invoke(this, values.toArray());
        } catch (Exception e) {
            throw new ExecutionException(e, "Unable to execute the command.");
        }
    }

}
