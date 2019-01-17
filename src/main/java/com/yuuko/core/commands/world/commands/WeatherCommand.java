package com.yuuko.core.commands.world.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.world.WorldModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Utils;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class WeatherCommand extends Command {

    public WeatherCommand() {
        super("weather", WorldModule.class, 1, new String[]{"-weather [city]", "-weather [city] [country]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            JsonObject data = new JsonBuffer("https://api.openweathermap.org/data/2.5/weather?q=" + (command[1].replace(" ", "+")) + "&units=metric&APPID=" + Utils.getApiKey("openweathermap"), "default", "default", null, null).getAsJsonObject();

            if(data == null) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + command[1] + "_** produced no results.");
                MessageHandler.sendMessage(e, embed.build());
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
                    .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName() , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
