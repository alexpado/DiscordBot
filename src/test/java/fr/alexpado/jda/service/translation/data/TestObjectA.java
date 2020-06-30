package fr.alexpado.jda.service.translation.data;

import fr.alexpado.jda.services.translations.annotations.I18N;

public class TestObjectA {

    @I18N("offline")
    private String discordBot;

    @I18N("online")
    private String translationService;

    @I18N
    private TestObjectB trSrv = new TestObjectB();

    public String getDiscordBot() {

        return this.discordBot;
    }

    public String getTranslationService() {

        return this.translationService;
    }

    public TestObjectB getTrSrv() {

        return this.trSrv;
    }
}
