package fr.alexpado.jda.service.translation;

import fr.alexpado.jda.service.translation.data.TestObjectA;
import fr.alexpado.jda.service.translation.data.TranslationProviderTest;
import fr.alexpado.jda.services.translations.Translator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Translations")
public class TranslationTests {

    @Test
    @DisplayName("English translation")
    public void testObjectEnglishTranslation() throws IllegalAccessException {

        TestObjectA objectA = new TestObjectA();
        TranslationProviderTest providerTest = new TranslationProviderTest();

        Translator.translate(providerTest, "en", objectA);

        assertEquals("Service Online", objectA.getTranslationService());
        assertEquals("Service Offline", objectA.getDiscordBot());
        assertEquals("Property", objectA.getTrSrv().getProps());
    }

    @Test
    @DisplayName("French translation")
    public void testObjectFrenchTranslation() throws IllegalAccessException {

        TestObjectA objectA = new TestObjectA();
        TranslationProviderTest providerTest = new TranslationProviderTest();

        Translator.translate(providerTest, "fr", objectA);

        assertEquals("Service en ligne", objectA.getTranslationService());
        assertEquals("Service hors ligne", objectA.getDiscordBot());
        assertEquals("Propriété", objectA.getTrSrv().getProps());
    }



}
