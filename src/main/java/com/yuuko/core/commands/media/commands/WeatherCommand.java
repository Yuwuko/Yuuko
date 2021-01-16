package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.api.entity.Api;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import com.yuuko.core.utilities.Sanitiser;
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
        super("weather", Yuuko.MODULES.get("media"), 1, -1L, Arrays.asList("-weather <city>", "-weather <city> <country>"), false, null, api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        final String url = BASE_URL + (Sanitiser.scrub(e.getParameters(), false).replace(" ", "+")) + "&units=metric&APPID=" + api.getKey();
        JsonObject data = new RequestHandler(url).getJsonObject();

        if(data == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for `" + e.getParameters() + "` produced no results.");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Weather information for: " + data.get("name").getAsString() + ", " + data.get("sys").getAsJsonObject().get("country").getAsString())
                .setImage("https://openweathermap.org/img/w/" + data.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString() + ".png")
                .setDescription("Please note that timezones given are GMT+0 between November/March and BST between April/October due to system time on the server.")
                .addField("ID", data.get("id").getAsString(), true)
                .addField("Visibility", data.get("visibility").getAsString() + "m", true)
                .addField("Humidity", data.get("main").getAsJsonObject().get("humidity").getAsString() + "%", true)
                .addField("Temperature", data.get("main").getAsJsonObject().get("temp").getAsString() + "c", true)
                .addField("Max Temperature", data.get("main").getAsJsonObject().get("temp_max").getAsString() + "c", true)
                .addField("Min Temperature", data.get("main").getAsJsonObject().get("temp_min").getAsString() + "c", true)
                .addField("Sunrise", LocalDateTime.ofEpochSecond(data.get("sys").getAsJsonObject().get("sunrise").getAsInt(), 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("hh:mma")), true)
                .addField("Latitude", data.get("coord").getAsJsonObject().get("lat").getAsString(), true)
                .addField("Wind Speed", data.get("wind").getAsJsonObject().get("speed").getAsString() + "mph", true)
                .addField("Sunset", LocalDateTime.ofEpochSecond(data.get("sys").getAsJsonObject().get("sunset").getAsInt(), 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("hh:mma")), true)
                .addField("Longitude", data.get("coord").getAsJsonObject().get("lon").getAsString(), true)
                .addField("Wind Angle", data.get("wind").getAsJsonObject().get("deg").getAsString() + "°", true)
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }

}
