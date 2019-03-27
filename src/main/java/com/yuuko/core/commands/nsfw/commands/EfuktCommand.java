package com.yuuko.core.commands.nsfw.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.nsfw.NSFWModule;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class EfuktCommand extends Command {

    public EfuktCommand() {
        super("efukt", NSFWModule.class, 0, new String[]{"-efukt"}, true, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            Document doc = Jsoup.connect("https://efukt.com/random.php").get();
            EmbedBuilder efuktPost;

            if(doc.getElementsByClass("image_content").isEmpty()) {
                efuktPost = new EmbedBuilder()
                        .setTitle("Efukt: " + doc.getElementsByTag("h1").text())
                        .setDescription(doc.getElementsByTag("source").attr("src"))
                        .setImage(doc.getElementsByTag("video").attr("poster"))
                        .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                MessageHandler.sendMessage(e, efuktPost.build());
            } else {
                efuktPost = new EmbedBuilder()
                        .setTitle("Efukt: " + doc.getElementsByTag("h1").text())
                        .setDescription(doc.getElementsByClass("image_content").attr("src"))
                        .setImage(doc.getElementsByClass("image_content").attr("src"))
                        .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                MessageHandler.sendMessage(e, efuktPost.build());
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
