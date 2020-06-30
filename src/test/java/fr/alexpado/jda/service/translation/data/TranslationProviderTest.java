package fr.alexpado.jda.service.translation.data;

import fr.alexpado.jda.services.translations.interfaces.ITranslation;
import fr.alexpado.jda.services.translations.interfaces.ITranslationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TranslationProviderTest implements ITranslationProvider {

    private List<ITranslation> translations = new ArrayList<>();

    public TranslationProviderTest() {

        this.translations.addAll(Arrays.asList(createTranslation("en", "offline", "Service Offline"), createTranslation("en", "online", "Service Online"), createTranslation("en", "props", "Property"), createTranslation("fr", "offline", "Service hors ligne"), createTranslation("fr", "online", "Service en ligne"), createTranslation("fr", "props", "Propriété")));
    }

    private static ITranslation createTranslation(String language, String key, String value) {

        return new ITranslation() {

            @Override
            public String getKey() {

                return key;
            }

            @Override
            public String getLanguage() {

                return language;
            }

            @Override
            public String getValue() {

                return value;
            }
        };
    }

    /**
     * Get the translation for the provided language and key using the list of args provided to format the raw
     * translation value.
     *
     * @param language
     *         The translation's language
     * @param key
     *         The translation's key
     *
     * @return The fully translated and formatted string corresponding to the language and key.
     */
    @Override
    public String getTranslation(String language, String key) {

        Optional<ITranslation> optionalITranslation = this.translations.stream()
                                                                       .filter(translation -> translation.getKey()
                                                                                                         .equals(key))
                                                                       .filter(translation -> translation.getLanguage()
                                                                                                         .equals(language))
                                                                       .findFirst();

        if (optionalITranslation.isEmpty()) {
            throw new IllegalArgumentException("The key provided doesn't exists in this language !");
        }

        return optionalITranslation.get().getValue();
    }
}
