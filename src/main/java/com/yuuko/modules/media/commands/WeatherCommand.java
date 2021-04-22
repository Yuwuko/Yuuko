package com.yuuko.modules.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class WeatherCommand extends Command {
    private static final Api api = Yuuko.API_MANAGER.getApi("openweathermap");
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";

    public WeatherCommand() {
        super("weather", Arrays.asList("-weather <city>", "-weather <city> <country>"), 1);
        setEnabled(api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        final String url = BASE_URL + (Sanitiser.scrub(context.getParameters(), false).replace(" ", "+")) + "&units=metric&APPID=" + api.getKey();
        JsonObject data = new RequestHandler(url).getJsonObject();

        if(data == null || data.isJsonNull() || data.size() < 1) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "no_results")).setDescription(context.i18n( "no_results_desc").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n( "title").formatted(data.get("name").getAsString(), data.get("sys").getAsJsonObject().get("country").getAsString()))
                .setImage("https://openweathermap.org/img/w/" + data.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString() + ".png")
                .setDescription(context.i18n( "desc"))
                .addField(context.i18n( "id"), data.get("id").getAsString(), true)
                .addField(context.i18n( "visibility"), data.get("visibility").getAsString() + "m", true)
                .addField(context.i18n( "humidity"), data.get("main").getAsJsonObject().get("humidity").getAsString() + "%", true)
                .addField(context.i18n( "temp"), data.get("main").getAsJsonObject().get("temp").getAsString() + "c", true)
                .addField(context.i18n( "max_temp"), data.get("main").getAsJsonObject().get("temp_max").getAsString() + "c", true)
                .addField(context.i18n( "min_temp"), data.get("main").getAsJsonObject().get("temp_min").getAsString() + "c", true)
                .addField(context.i18n( "sunrise"), LocalDateTime.ofEpochSecond(data.get("sys").getAsJsonObject().get("sunrise").getAsInt(), 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("hh:mma")), true)
                .addField(context.i18n( "latitude"), data.get("coord").getAsJsonObject().get("lat").getAsString(), true)
                .addField(context.i18n( "wind_speed"), data.get("wind").getAsJsonObject().get("speed").getAsString() + "mph", true)
                .addField(context.i18n( "sunset"), LocalDateTime.ofEpochSecond(data.get("sys").getAsJsonObject().get("sunset").getAsInt(), 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("hh:mma")), true)
                .addField(context.i18n( "longitude"), data.get("coord").getAsJsonObject().get("lon").getAsString(), true)
                .addField(context.i18n( "wind_angle"), data.get("wind").getAsJsonObject().get("deg").getAsString() + "°", true)
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }

}
