package fr.alexpado.jda.services.commands.interfaces;

import org.jetbrains.annotations.NotNull;

public interface IFilterMode {

    boolean isStrict();

    boolean isMatching(@NotNull String strA, @NotNull String strB);

}
