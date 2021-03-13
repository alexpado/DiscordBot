package fr.alexpado.jda.services.commands.interfaces;

import net.dv8tion.jda.api.entities.Message;

public interface ICommandMeta {

    /**
     * Retrieves the label associated with an {@link ICommand}.
     *
     * @return A label.
     */
    String getLabel();

    /**
     * Retrieves the description associated with an {@link ICommand}. This will be used in the help menu.
     *
     * @return A description.
     */
    String getDescription();

    /**
     * Retrieves the help associated with an {@link ICommand}. This will be used in the advanced help menu.
     *
     * @return A help.
     */
    String getHelp();

    /**
     * Determines if the {@link Message} triggering the {@link ICommand} associated should be delete once handled.
     *
     * @return True if the {@link Message} should be deleted, false otherwise.
     */
    default boolean shouldDeleteMessage() {

        return false;
    }

    /**
     * Determines if the {@link Message} sent as a result of the {@link ICommand} associated should be deleted after a
     * certain amount of time.
     *
     * @return True if the {@link Message} should be deleted, false otherwise.
     */
    default boolean shouldDeleteResult() {

        return false;
    }

}
