package com.yuuko.core.modules.world.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.world.weather.WeatherContainer;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import com.yuuko.core.utils.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CommandWeather extends Command {

    public CommandWeather() {
        super("weather", "com.yuuko.core.modules.world.ModuleWorld", 1, new String[]{"-weather [city]", "-weather [city] [country]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            command[1] = command[1].replace(" ", "+");
            String json = new JsonBuffer("https://api.openweathermap.org/data/2.5/weather?q=" +command[1] + "&units=metric&APPID=" + Utils.getApiKey("openweathermap"), "default", "default", null, null).getAsString();

            if(json != null && json.equals("")) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Sorry, **_" + command[1] + "_** returned no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            WeatherContainer weather = new ObjectMapper().readValue(json, new TypeReference<WeatherContainer>(){});

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Weather information for: " + weather.getName() + ", " + weather.getSys().getCountry())
                    .setImage("https://openweathermap.org/img/w/" + weather.getWeather().get(0).getIcon() + ".png")
                    .setDescription("Please note that timezones given are GMT+0 between November/March and BST between April/October due to system time on the server.")
                    .addField("ID", weather.getId() + "", true)
                    .addField("Visibility", weather.getVisibility() + "m", true)
                    .addField("Humidity", weather.getMain().getHumidity() + "%", true)
                    .addField("Temperature", weather.getMain().getTemp() + "c", true)
                    .addField("Max Temperature", weather.getMain().getTempMax() + "c", true)
                    .addField("Min Temperature", weather.getMain().getTempMin() + "c", true)
                    .addField("Sunrise", LocalDateTime.ofEpochSecond(weather.getSys().getSunrise(), 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("hh:mma")), true)
                    .addField("Latitude", weather.getCoord().getLat() + "", true)
                    .addField("Wind Speed", weather.getWind().getSpeed() + "mph", true)
                    .addField("Sunset", LocalDateTime.ofEpochSecond(weather.getSys().getSunset(), 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("hh:mma")), true)
                    .addField("Longitude", weather.getCoord().getLon() + "", true)
                    .addField("Wind Angle", weather.getWind().getDeg() + "°", true)
                    .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName() , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            MessageHandler.sendException(ex, command[0] + command[1]);
        }
    }

}
