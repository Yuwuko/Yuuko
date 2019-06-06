package com.yuuko.core.commands.nsfw.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.nsfw.NsfwModule;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;

public class EfuktCommand extends Command {

    public EfuktCommand() {
        super("efukt", NsfwModule.class, 0, Arrays.asList("-efukt"), true, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            Document doc = Jsoup.connect("https://efukt.com/random.php").get();
            Elements meta = doc.getElementsByTag("meta");

            String image = "https://i.imgur.com/YXqsEo6.jpg";
            if(doc.baseUri().startsWith("https://efukt.com/view.gif.php")) {
                image = doc.getElementsByClass("image_content").attr("src");
            } else {
                for(Element tag : meta) {
                    if(tag.hasAttr("property") && tag.attr("property").equals("og:image")) {
                        image = tag.attr("content");
                    }
                }
            }

            EmbedBuilder efuktPost = new EmbedBuilder()
                    .setTitle(doc.title())
                    .setDescription(doc.baseUri())
                    .setImage(image)
                    .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, efuktPost.build());

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
