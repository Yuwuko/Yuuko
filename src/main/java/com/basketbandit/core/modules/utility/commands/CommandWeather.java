package com.basketbandit.core.modules.utility.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.utility.commands.weather.Weather;
import com.basketbandit.core.utils.Utils;
import com.basketbandit.core.utils.json.JsonBuffer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.Date;

public class CommandWeather extends Command {

    public CommandWeather() {
        super("weather", "com.basketbandit.core.modules.utility.ModuleUtility", new String[]{"-weather [city]", "-weather [city] [country]"}, null);
    }

    public CommandWeather(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 2);
            commandParameters[0] = command[1].replace(" ", "+");

            String json = new JsonBuffer().getString("https://api.openweathermap.org/data/2.5/weather?q=" +commandParameters[0] + "&units=metric&APPID=" + Configuration.OPEN_WEATHER_MAP_API);

            if(json != null && json.equals("")) {
                Utils.sendMessage(e,"Sorry " + e.getAuthor().getAsMention() + ", unable to retrieve weather information from " + command[1] + ".");
                return;
            }

            Weather weather = new ObjectMapper().readValue(json, new TypeReference<Weather>(){});

            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setTitle("Weather information for: " + weather.getName() + ", " + weather.getSys().getCountry())
                    .setThumbnail("https://openweathermap.org/img/w/" + weather.getWeather().get(0).getIcon() + ".png")
                    .addField("ID", weather.getId() + "", true)
                    .addField("Visibility", weather.getVisibility() + "m", true)
                    .addField("Latitude", weather.getCoord().getLat() + "", true)
                    .addField("Longitude", weather.getCoord().getLon() + "", true)
                    .addField("Sunrise", Date.from(Instant.ofEpochSecond(weather.getSys().getSunrise())) + "", true)
                    .addField("Sunset", Date.from(Instant.ofEpochSecond(weather.getSys().getSunset())) + "", true)
                    .addField("Humidity", weather.getMain().getHumidity() + "%", true)
                    .addField("Temperature", weather.getMain().getTemp() + "c", true)
                    .addField("Max Temperature", weather.getMain().getTempMax() + "c", true)
                    .addField("Min Temperature", weather.getMain().getTempMin() + "c", true)
                    .addField("Wind Speed", weather.getWind().getSpeed() + "mph", true)
                    .addField("Wind Angle", weather.getWind().getDeg() + "°", true)
                    .setFooter("Version: " + Configuration.VERSION + ", Data provided by openweathermap.org" , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            Utils.sendMessage(e, embed.build());

        } catch(Exception ex) {
            ex.printStackTrace();
            Utils.sendMessage(e, "There was an issue processing the request for command: " + e.getMessage().getContentDisplay());
        }
    }
}
