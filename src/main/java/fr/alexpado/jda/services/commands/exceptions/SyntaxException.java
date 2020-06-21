package fr.alexpado.jda.services.commands.exceptions;

import fr.alexpado.jda.services.commands.annotations.Command;

/**
 * Exception used when the syntax deduced from the user's input didn't match any method annotated with {@link Command}.
 *
 * @author alexpado
 */
public class SyntaxException extends CommandException {

    public SyntaxException(String message) {

        super(message);
    }

}
