package com.yuuko.core.commands.nsfw.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.nsfw.NSFWModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class EfuktCommand extends Command {

    public EfuktCommand() {
        super("efukt", NSFWModule.class, 0, new String[]{"-efukt"}, true, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {
            Document doc = Jsoup.connect("https://efukt.com/random.php").get();
            EmbedBuilder efuktPost;

            if(doc.getElementsByClass("image_content").isEmpty()) {
                efuktPost = new EmbedBuilder()
                        .setTitle("Efukt: " + doc.getElementsByTag("h1").text())
                        .setDescription(doc.getElementsByTag("source").attr("src"))
                        .setImage(doc.getElementsByTag("video").attr("poster"))
                        .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), Cache.BOT.getAvatarUrl());
                MessageHandler.sendMessage(e.getTextChannel(), efuktPost.build());
            } else {
                efuktPost = new EmbedBuilder()
                        .setTitle("Efukt: " + doc.getElementsByTag("h1").text())
                        .setDescription(doc.getElementsByClass("image_content").attr("src"))
                        .setImage(doc.getElementsByClass("image_content").attr("src"))
                        .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), Cache.BOT.getAvatarUrl());
                MessageHandler.sendMessage(e.getTextChannel(), efuktPost.build());
            }

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "EfuktCommand");
        }
    }

}
