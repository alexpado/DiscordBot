package fr.alexpado.jda.services.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation allowing to identify a method used to execute commands. The method annotated with {@link Command} needs to
 * be public.
 *
 * @author alexpado
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /**
     * Syntax string that will be used to trigger the annotated method.
     *
     * @return A syntax string
     */
    String value();

}
