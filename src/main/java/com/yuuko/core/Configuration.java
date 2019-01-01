package com.yuuko.core;

import com.yuuko.core.utils.MessageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Configuration {
    public static final String VERSION = "Y-1.0.1";
    public static String BOT_ID;
    static String BOT_TOKEN;
    public static final String GLOBAL_PREFIX = "@Yuuko#2525";
    static String STATUS = "@Yuuko help";
    public static String DATABASE_IP;
    public static String DATABASE_NAME;
    public static String DATABASE_USERNAME;
    public static String DATABASE_PASSWORD;
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
            MessageHandler.sendException(ex, "Configuration.load()");
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
                System.out.println("[INFO] " + keyFiles.length + " API keys successfully loaded.");
            }

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "loadAPI");
        }
    }

}
