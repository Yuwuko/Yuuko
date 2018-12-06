package com.yuuko.core;

import com.yuuko.core.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Configuration {
    // Bot version.
    public static final String VERSION = "Y-1.0.0";
    // Bot's ID.
    public static String BOT_ID;
    // Bot's Token.
    static String BOT_TOKEN;
    // Bot's global command prefix.
    public static final String GLOBAL_PREFIX = "@Yuuko#2525";
    // Bot's status, e.g Playing with lottie's tits.
    static String STATUS = "@Yuuko help";
    // Database IP:PORT
    public static String DATABASE_IP;
    // Database Name
    public static String DATABASE_NAME;
    // Database Username
    public static String DATABASE_USERNAME;
    // Database Password
    public static String DATABASE_PASSWORD;
    // API key list (Yes, a HashMap is better, but I forgot and put effort into home-brew!)
    public static HashMap<String, ApplicationProgrammingInterface> API_KEYS;

    static void load() {
        try {
            BufferedReader c = new BufferedReader(new FileReader("configuration.txt"));
            BOT_ID = c.readLine();
            BOT_TOKEN = c.readLine();
            DATABASE_IP = c.readLine();
            DATABASE_NAME = c.readLine();
            DATABASE_USERNAME = c.readLine();
            DATABASE_PASSWORD = c.readLine();
            c.close();

        } catch(Exception ex) {
            Utils.sendException(ex, "Configuration.load()");
        }
    }

    public static void loadApi() {
        try {
            File folder = new File("./api/");
            File[] keyFiles = folder.listFiles();

            if(keyFiles != null) {
                API_KEYS = new HashMap<>();
                for(File key : keyFiles) {
                    BufferedReader c = new BufferedReader(new FileReader(key));
                    API_KEYS.put(key.getName(), new ApplicationProgrammingInterface(key.getName(), c.readLine(), c.readLine()));
                    c.close();
                }
                System.out.println("Loaded " + keyFiles.length + " API keys.");
            }

        } catch(Exception ex) {
            Utils.sendException(ex, "loadAPI");
        }
    }

}
