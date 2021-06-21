package com.yuuko.modules.fun.commands;

import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class HoroscopeCommand extends Command {
    private static final String BASE_URL = "http://ohmanda.com/api/horoscope/";
    private static final List<String> starsigns = Arrays.asList(
            "aquarius",
            "pisces",
            "aries",
            "taurus",
            "gemini",
            "cancer",
            "leo",
            "virgo",
            "libra",
            "scorpio",
            "sagittarius",
            "capricorn"
    );

    public HoroscopeCommand() {
        super("horoscope", Arrays.asList("-horoscope <sign>"), 1);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        String selectedSign = null;
        for(String sign: starsigns) {
            if(sign.contains(context.getParameters().toLowerCase())) {
                selectedSign = sign;
                break;
            }
        }

        if(selectedSign == null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n( "invalid_title"))
                    .setDescription(context.i18n( "invalid_desc").formatted(context.getParameters(), starsigns.toString()));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        final JsonObject object = new RequestHandler(BASE_URL + selectedSign).getJsonObject();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Horoscope - " + selectedSign + " - " + object.get("date").getAsString())
                .setDescription(object.get("horoscope").getAsString());
        MessageDispatcher.reply(context, embed.build());
    }
}
