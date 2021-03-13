package fr.alexpado.jda.services.translations;

import fr.alexpado.jda.services.translations.annotations.I18N;
import fr.alexpado.jda.services.translations.interfaces.ITranslationProvider;

import java.lang.reflect.Field;

public final class Translator {

    public static void translate(ITranslationProvider provider, String language, Object target) throws IllegalAccessException {

        for (Field declaredField : target.getClass().getDeclaredFields()) {

            I18N i18n = declaredField.getAnnotation(I18N.class);

            if (i18n == null) {
                continue;
            }

            if (!i18n.value().isEmpty() && declaredField.getType() == String.class) {
                // Direct translation
                String translation = provider.getTranslation(language, i18n.value());

                if (declaredField.canAccess(target)) {
                    declaredField.set(target, translation);
                } else {
                    declaredField.setAccessible(true);
                    declaredField.set(target, translation);
                    declaredField.setAccessible(false);
                }

            } else if (i18n.value().isEmpty()) {
                // Indirect translation
                if (declaredField.canAccess(target)) {
                    translate(provider, language, declaredField.get(target));
                } else {
                    declaredField.setAccessible(true);
                    translate(provider, language, declaredField.get(target));
                    declaredField.setAccessible(false);
                }
            }

        }
    }

}
