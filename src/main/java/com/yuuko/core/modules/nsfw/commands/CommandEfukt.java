package com.yuuko.core.modules.nsfw.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CommandEfukt extends Command {

    public CommandEfukt() {
        super("efukt", "com.yuuko.core.modules.nsfw.ModuleNSFW", 0, new String[]{"-efukt"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            Document doc = Jsoup.connect("https://efukt.com/random.php").get();
            EmbedBuilder efuktPost;

            if(doc.getElementsByClass("image_content").isEmpty()) {
                efuktPost = new EmbedBuilder()
                        .setTitle("Efukt: " + doc.getElementsByTag("h1").text())
                        .setDescription(doc.getElementsByTag("source").attr("src"))
                        .setImage(doc.getElementsByTag("video").attr("poster"))
                        .setFooter(Cache.STANDARD_STRINGS[2] + e.getAuthor().getName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                MessageHandler.sendMessage(e.getTextChannel(), efuktPost.build());
            } else {
                efuktPost = new EmbedBuilder()
                        .setTitle("Efukt: " + doc.getElementsByTag("h1").text())
                        .setDescription(doc.getElementsByClass("image_content").attr("src"))
                        .setImage(doc.getElementsByClass("image_content").attr("src"))
                        .setFooter(Cache.STANDARD_STRINGS[2] + e.getAuthor().getName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                MessageHandler.sendMessage(e.getTextChannel(), efuktPost.build());
            }

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "CommandEfukt");
        }
    }

}
