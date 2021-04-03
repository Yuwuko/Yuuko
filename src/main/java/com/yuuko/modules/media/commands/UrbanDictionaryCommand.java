package com.yuuko.modules.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class UrbanDictionaryCommand extends Command {
    private static final String BASE_URL = "https://api.urbandictionary.com/v0/define?term=";

    public UrbanDictionaryCommand() {
        super("urban", 1, -1L, Arrays.asList("-urban <term>"), true, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        final String url = BASE_URL + Sanitiser.scrub(e.getParameters(), true);
        final JsonObject json = new RequestHandler(url).getJsonObject();

        if(json == null || json.isJsonNull() || json.get("list").getAsJsonArray().size() < 1) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "no_results")).setDescription(I18n.getText(e, "no_results_desc").formatted(e.getParameters()));
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        JsonObject data = json.get("list").getAsJsonArray().get(0).getAsJsonObject();
        double thumbsUp = data.get("thumbs_up").getAsDouble();
        double thumbsDown = data.get("thumbs_down").getAsDouble();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(data.get("word").getAsString(), data.get("permalink").getAsString())
                .setDescription(data.get("definition").getAsString().replace("[", "").replace("]", ""))
                .addField(I18n.getText(e, "example"), data.get("example").getAsString().replace("[", "").replace("]", ""), false)
                .setFooter("👍 " + data.get("thumbs_up").getAsString() + " 👎 " + data.get("thumbs_down").getAsString() + " 📌 " + BigDecimal.valueOf((thumbsUp / (thumbsUp + thumbsDown)) * 100).setScale(2, RoundingMode.HALF_UP) + "%", null);
        MessageDispatcher.reply(e, embed.build());
    }

}
