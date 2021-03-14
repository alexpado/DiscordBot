package fr.alexpado.jda.services.commands;

import fr.alexpado.jda.services.commands.annotations.Param;
import fr.alexpado.jda.services.commands.interfaces.ICommandContext;
import fr.alexpado.jda.services.syntax.interfaces.IMatchingResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

final class CommandUtils {

    /**
     * Create the {@link List} of parameters that will be used to call the {@link Method} that is contained in the
     * {@link IMatchingResult} object.
     *
     * @param matchingResult
     *         The {@link IMatchingResult} for which the {@link List} should be created.
     * @param context
     *         The current context.
     *
     * @return A {@link List} of parameters to use.
     */
    public static List<Object> createParameters(IMatchingResult<Method> matchingResult, ICommandContext context) {

        Map<Class<?>, Object> paramTypeMap = new HashMap<>();

        paramTypeMap.put(User.class, context.getEvent().getAuthor());
        paramTypeMap.put(Message.class, context.getEvent().getMessage());
        paramTypeMap.put(Guild.class, context.getEvent().getGuild());
        paramTypeMap.put(Member.class, context.getEvent().getMember());
        paramTypeMap.put(TextChannel.class, context.getEvent().getChannel());
        paramTypeMap.put(MessageChannel.class, context.getEvent().getChannel());
        paramTypeMap.put(GuildChannel.class, context.getEvent().getChannel());
        paramTypeMap.put(JDA.class, context.getEvent().getJDA());
        paramTypeMap.put(GuildMessageReceivedEvent.class, context.getEvent());
        paramTypeMap.put(ICommandContext.class, context);

        List<Object> methodParameters = new ArrayList<>();

        for (Parameter parameter : matchingResult.getIdentifier().getParameters()) {
            if (parameter.isAnnotationPresent(Param.class)) {
                Param param = parameter.getAnnotation(Param.class);

                Optional<String> optionalValue = matchingResult.getParameter(param.value());

                if (parameter.getType() == Optional.class) {
                    methodParameters.add(optionalValue);
                } else {
                    methodParameters.add(optionalValue.orElse(null));
                }
            } else if (paramTypeMap.containsKey(parameter.getType())) {
                methodParameters.add(paramTypeMap.get(parameter.getType()));
            } else if (parameter.getType().isInstance(context)) {
                methodParameters.add(context);
            }
        }

        return methodParameters;
    }
}
