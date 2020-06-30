package fr.alexpado.jda.service.translation.data;

import fr.alexpado.jda.services.translations.annotations.I18N;

public class TestObjectB {

    @I18N("props")
    private String props;

    public String getProps() {

        return this.props;
    }
}
