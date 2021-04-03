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

public class EfuktCommand extends Command {
    private static final String BASE_URL = "https://efukt.com/random.php";
    private String image = "https://i.imgur.com/YXqsEo6.jpg";

    public EfuktCommand() {
        super("efukt", 0, -1L, Arrays.asList("-efukt"), true, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        Document doc = new RequestHandler(BASE_URL).getDocument();
        if(doc.baseUri().startsWith("https://efukt.com/view.gif.php")) {
            image = doc.getElementsByClass("image_content").attr("src");
        } else {
            Elements meta = doc.getElementsByTag("meta");
            for(Element tag: meta) {
                if(tag.hasAttr("property") && tag.attr("property").equals("og:image")) {
                    image = tag.attr("content");
                }
            }
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(doc.title().substring(0, Math.min(doc.title().length(), 256)))
                .setDescription(doc.baseUri())
                .setImage(image)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }

}
