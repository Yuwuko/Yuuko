package com.yuuko.commands.fun.commands;

import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
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
        super("horoscope", Yuuko.MODULES.get("fun"), 1, -1L, Arrays.asList("-horoscope <sign>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        String selectedSign = null;
        for(String sign: starsigns) {
            if(sign.contains(e.getParameters().toLowerCase())) {
                selectedSign = sign;
                break;
            }
        }

        if(selectedSign == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input.").setDescription("`" + e.getParameters() + "` is not as a valid sign. Select from `" + starsigns.toString() + "`");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        final JsonObject object = new RequestHandler(BASE_URL + selectedSign).getJsonObject();
        EmbedBuilder embed = new EmbedBuilder().setTitle("Horoscope - " + selectedSign + " - " + object.get("date").getAsString())
                .setDescription(object.get("horoscope").getAsString());
        MessageDispatcher.reply(e, embed.build());
    }
}
