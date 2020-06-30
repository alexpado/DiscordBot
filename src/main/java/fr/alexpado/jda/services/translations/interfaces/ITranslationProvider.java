package fr.alexpado.jda.services.translations.interfaces;


public interface ITranslationProvider {

    /**
     * Get the translation for the provided language and key using the list of args provided to format the raw
     * translation value.
     *
     * @param language
     *         The translation's language
     * @param key
     *         The translation's key
     * @param args
     *         The list of args to use when calling {@link ITranslation#getValue(Object...)}.
     *
     * @return The fully translated and formatted string corresponding to the language and key.
     */
    String getTranslation(String language, String key);


}
