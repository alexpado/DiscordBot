package fr.alexpado.jda.services.completion;

public enum FilterMode {
    STRICT(true), STARTING_WITH(false);


    final boolean strict;

    FilterMode(boolean strict) {
        this.strict = strict;
    }

    public boolean isStrict() {
        return this.strict;
    }

    public boolean isMatching(String a, String b) {
        if (this.isStrict()) {
            return a.equalsIgnoreCase(b);
        } else {
            return a.toLowerCase().startsWith(b.toLowerCase());
        }
    }

}
