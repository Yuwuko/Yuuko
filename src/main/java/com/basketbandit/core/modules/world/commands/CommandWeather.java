package com.basketbandit.core.modules.world.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.world.weather.WeatherContainer;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import com.basketbandit.core.utils.json.JsonBuffer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CommandWeather extends Command {

    public CommandWeather() {
        super("weather", "com.basketbandit.core.modules.world.ModuleWorld", 1, new String[]{"-weather [city]", "-weather [city] [country]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            command[1] = command[1].replace(" ", "+");
            String json = new JsonBuffer().getString("https://api.openweathermap.org/data/2.5/weather?q=" +command[1] + "&units=metric&APPID=" + Configuration.OPEN_WEATHER_MAP_API, "default", "default");

            if(json != null && json.equals("")) {
                MessageHandler.sendMessage(e,"Sorry " + e.getAuthor().getAsMention() + ", unable to retrieve weather information from " + command[1] + ".");
                return;
            }

            WeatherContainer weather = new ObjectMapper().readValue(json, new TypeReference<WeatherContainer>(){});

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("WeatherContainer information for: " + weather.getName() + ", " + weather.getSys().getCountry())
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
                    .setFooter(Configuration.VERSION + " · Data provided by openweathermap.org" , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            Utils.sendException(ex, command[0] + command[1]);
        }
    }

}
