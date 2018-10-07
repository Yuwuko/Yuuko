package com.basketbandit.core;

import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.JDA;

import java.io.BufferedReader;
import java.io.FileReader;

public class Configuration {
    // Bot object.
    public static JDA BOT;
    // Bot version.
    public static final String VERSION = "3.2.2";
    // Bot's ID.
    public static String BOT_ID;
    // Bot's Token.
    static String BOT_TOKEN;
    // Bot's global command prefix.
    public static String GLOBAL_PREFIX;
    // Bot's status, e.g Playing with lottie's tits.
    static String STATUS = "@BasketBandit help";
    // Database IP:PORT
    public static String DATABASE_IP;
    // Database Name
    public static String DATABASE_NAME;
    // Database Username
    public static String DATABASE_USERNAME;
    // Database Password
    public static String DATABASE_PASSWORD;
    // Google API key.
    public static String GOOGLE_API;
    // TFL App ID.
    public static String TFL_ID;
    // TFL API Key.
    public static String TFL_API;
    // Discord Bot List API.
    static String DISCORD_BOTS_API;
    // WoW API key.
    public static String WOW_API;
    // Osu API Key
    public static String OSU_API;
    // Open WeatherContainer Map API key
    public static String OPEN_WEATHER_MAP_API;

    static void load() {
        try {
            BufferedReader c = new BufferedReader(new FileReader("configuration.txt"));
            BOT_ID = c.readLine();
            BOT_TOKEN = c.readLine();
            GOOGLE_API = c.readLine();
            TFL_ID = c.readLine();
            TFL_API = c.readLine();
            WOW_API = c.readLine();
            DATABASE_IP = c.readLine();
            DATABASE_NAME = c.readLine();
            DATABASE_USERNAME = c.readLine();
            DATABASE_PASSWORD = c.readLine();
            OSU_API = c.readLine();
            OPEN_WEATHER_MAP_API = c.readLine();
            DISCORD_BOTS_API = c.readLine();
            c.close();

        } catch(Exception ex) {
            Utils.sendException(ex, "Configuration.load()");
        }
    }

}
