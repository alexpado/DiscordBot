package fr.alexpado.jda.services.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark a method's parameter as a parameter receiving a dynamic a value coming from the
 * user's input.
 *
 * @author alexpado
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Flags {}
