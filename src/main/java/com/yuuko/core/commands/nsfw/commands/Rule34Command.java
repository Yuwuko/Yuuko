package com.yuuko.core.commands.nsfw.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;

public class Rule34Command extends Command {
    private static final String BASE_URL = "https://rule34.xxx/index.php?page=post&s=random";

    public Rule34Command() {
        super("rule34", Config.MODULES.get("nsfw"), 0, -1L, Arrays.asList("-rule34"), true, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            Document doc = new RequestHandler(BASE_URL).getDocument();
            Elements images = doc.getElementsByTag("img");

            String image = "https://i.imgur.com/YXqsEo6.jpg";
            for(Element img: images) {
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

            EmbedBuilder efuktPost = new EmbedBuilder()
                    .setTitle("Rule 34" + (!characterString.toString().equals("") ? characterString.substring(0, characterString.length() - 2) : ""))
                    .setImage(image)
                    .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, efuktPost.build());

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
