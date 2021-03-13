package fr.alexpado.jda.services.tools.interfaces;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface IEmbedPage<T> {

    /**
     * Retrieve the {@link EmbedBuilder} that will be used as base to generate the {@link IEmbedPage}.
     *
     * @param jda
     *         A {@link JDA} instance.
     *
     * @return The {@link EmbedBuilder} that will be used as base.
     */
    EmbedBuilder getEmbed(JDA jda);

    /**
     * Retrieve the provided {@link T} as {@link MessageEmbed.Field}.
     *
     * @param index
     *         The index of the element in the list.
     * @param item
     *         The item.
     *
     * @return A {@link MessageEmbed.Field} representing the element.
     */
    MessageEmbed.Field getFieldFor(int index, T item);

}
