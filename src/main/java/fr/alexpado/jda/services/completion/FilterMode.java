package fr.alexpado.jda.services.completion;

import fr.alexpado.jda.services.commands.interfaces.IFilterMode;
import org.jetbrains.annotations.NotNull;

public enum FilterMode implements IFilterMode {

    STRICT(true),
    STARTING_WITH(false);


    final boolean strict;

    FilterMode(boolean strict) {

        this.strict = strict;
    }

    @Override
    public boolean isStrict() {

        return this.strict;
    }

    @Override
    public boolean isMatching(@NotNull String strA, @NotNull String strB) {

        if (this.isStrict()) {
            return strA.equalsIgnoreCase(strB);
        } else {
            return strA.toLowerCase().startsWith(strB.toLowerCase());
        }
    }

}
