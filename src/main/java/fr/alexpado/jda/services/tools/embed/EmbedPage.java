package fr.alexpado.jda.services.tools.embed;

import fr.alexpado.jda.services.tools.interfaces.IEmbedPage;
import fr.alexpado.jda.services.tools.reaction.ReactionListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class EmbedPage<T> extends ReactionListener implements IEmbedPage<T> {

    private final List<T> origin;
    private final int     count       = 6;
    private       int     currentPage = 1;
    private       int     totalPage   = 1;

    protected EmbedPage(List<T> items, int timeout) {

        super(null, timeout);
        this.origin = items;
    }

    @Override
    public void setMessage(@NotNull Message message) {

        super.setMessage(message);
        this.currentPage = 1;

        float pageIndex = (float) this.origin.size() / (float) this.count;
        int   page      = this.origin.size() / this.count;

        if (pageIndex > page) {
            this.totalPage = page + 1;
        } else {
            this.totalPage = page;
        }
        this.totalPage = this.totalPage == 0 ? 1 : this.totalPage;

        if (this.origin.size() > this.count) {

            this.addAction("◀", reactionAction -> {
                this.previousPage(reactionAction.getReaction().getJDA());
                reactionAction.getReaction().removeReaction(reactionAction.getUser()).queue();
            });

            this.addAction("▶", reactionAction -> {
                this.nextPage(reactionAction.getReaction().getJDA());
                reactionAction.getReaction().removeReaction(reactionAction.getUser()).queue();
            });

            this.refreshEmbed(message.getJDA());
            this.start();
        } else {
            this.refreshEmbed(message.getJDA());
        }
    }

    protected void refreshEmbed(JDA jda) {

        this.resetTimer();

        EmbedBuilder embed = this.getEmbed(jda);

        for (MessageEmbed.Field field : this.getPageContent()) {
            embed.addField(field);
        }

        if (this.totalPage > 1) {
            embed.setFooter(String.format("Page %s / %s", this.currentPage, this.totalPage));
        }

        if (this.getMessage() != null) {
            this.getMessage().editMessage(embed.build()).queue();
        }
    }

    private boolean isPageValid() {

        return this.currentPage > 0 && this.currentPage <= this.totalPage;
    }

    private List<MessageEmbed.Field> getPageContent() {

        List<MessageEmbed.Field> fields = new ArrayList<>();

        int[] pageBound = this.getPageBound(this.origin.size(), this.currentPage, this.count);

        if (pageBound == null) {
            return fields;
        }

        for (int i = pageBound[0] ; i < pageBound[1] ; i++) {
            fields.add(this.getFieldFor((i + 1) - pageBound[0], this.origin.get(i)));
        }

        return fields;
    }

    private void nextPage(JDA jda) {

        this.currentPage++;

        if (this.isPageValid()) {
            this.refreshEmbed(jda);
        } else {
            this.currentPage--;
        }
    }

    private void previousPage(JDA jda) {

        this.currentPage--;

        if (this.isPageValid()) {
            this.refreshEmbed(jda);
        } else {
            this.currentPage++;
        }
    }

    private int[] getPageBound(int size, int page, int count) {

        int startAt = (page - 1) * count;
        if (startAt > size || startAt < 0) {
            return null;
        }

        int endAt = startAt + count;
        if (endAt > size) {
            endAt = size;
        }
        return new int[]{startAt, endAt};
    }

}
