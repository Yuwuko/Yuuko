package com.basketbandit.core.modules.nsfw.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;

public class CommandEfukt extends Command {

    public CommandEfukt() {
        super("efukt", "com.basketbandit.core.modules.nsfw.ModuleNSFW", 0, new String[]{"-efukt"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            Document doc = Jsoup.connect("https://efukt.com/random.php").get();
            EmbedBuilder efuktPost;

            if(doc.getElementsByClass("image_content").isEmpty()) {
                efuktPost = new EmbedBuilder()
                        .setColor(Color.DARK_GRAY)
                        .setTitle(doc.getElementsByTag("h1").text())
                        .setDescription(doc.getElementsByTag("source").attr("src"))
                        .setImage(doc.getElementsByTag("video").attr("poster"))
                        .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                Utils.sendMessage(e.getTextChannel(), efuktPost.build());
            } else {
                efuktPost = new EmbedBuilder()
                        .setColor(Color.DARK_GRAY)
                        .setTitle(doc.getElementsByTag("h1").text())
                        .setDescription(doc.getElementsByClass("image_content").attr("src"))
                        .setImage(doc.getElementsByClass("image_content").attr("src"))
                        .setFooter(Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                Utils.sendMessage(e.getTextChannel(), efuktPost.build());
            }

        } catch(Exception ex) {
            Utils.sendException(ex, command[0]);
        }
    }

}
