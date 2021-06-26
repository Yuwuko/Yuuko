package com.yuuko.modules.nsfw.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;

public class Rule34Command extends Command {
    private static final String BASE_URL = "https://rule34.xxx/index.php?page=post&s=random";
    private String image = "https://i.imgur.com/YXqsEo6.jpg";

    public Rule34Command() {
        super("rule34", Arrays.asList("-rule34"), true);
    }

    @Override
    public void onCommand(MessageEvent context) {
        Document doc = new RequestHandler(BASE_URL).getDocument();
        Elements images = doc.getElementsByTag("img");
        for(Element img : images) {
            if(img.hasAttr("height")) {
                image = img.attr("src");
                break;
            }
        }

        // Retrieves character names.
        Elements characters = doc.getElementById("tag-sidebar").getElementsByClass("tag-type-character");
        StringBuilder characterString = new StringBuilder();
        if(characters.size() > 0) {
            characterString = new StringBuilder(" - ft. ");
            for(Element tag : characters.get(0).children()) {
                if(tag.tagName().equals("a")) {
                    characterString.append(tag.text()).append(", ");
                }
            }
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Rule 34" + (!characterString.toString().equals("") ? characterString.substring(0, characterString.length() - 2) : ""))
                .setImage(image)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }

}
