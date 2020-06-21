package fr.alexpado.jda.services.commands.exceptions;

import fr.alexpado.jda.services.commands.annotations.Command;

/**
 * Exception used when the invocation of a method annotated with {@link Command} fails.
 *
 * @author alexpado
 */
public class ExecutionException extends CommandException {

    private final Exception cause;

    public ExecutionException(Exception cause, String message) {

        super(message);
        this.cause = cause;
    }

    @Override
    public Exception getCause() {

        return this.cause;
    }

}
