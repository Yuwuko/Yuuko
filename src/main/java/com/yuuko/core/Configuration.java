package com.yuuko.core;

import com.yuuko.core.utilities.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Configuration {
    public static final String VERSION = "05-02-2019_1";
    public static String AUTHOR;
    public static String AUTHOR_WEBSITE;
    public static String SUPPORT_GUILD;
    public static String BOT_ID;
    static String BOT_TOKEN;
    public static int SHARD_COUNT = 0;
    public static int SHARD_ID = 0;
    public static String GLOBAL_PREFIX;
    static String STATUS = "@Yuuko help";
    public static String DATABASE_IP;
    public static String DATABASE_NAME;
    public static String DATABASE_USERNAME;
    public static String DATABASE_PASSWORD;
    public static HashMap<String, ApplicationProgrammingInterface> API_KEYS;

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    static void load() {
        try {
            BufferedReader c = new BufferedReader(new FileReader("configuration.txt"));
            AUTHOR = c.readLine();
            AUTHOR_WEBSITE = c.readLine();
            SUPPORT_GUILD = c.readLine();
            BOT_ID = c.readLine();
            BOT_TOKEN = c.readLine();
            DATABASE_IP = c.readLine();
            DATABASE_NAME = c.readLine();
            DATABASE_USERNAME = c.readLine();
            DATABASE_PASSWORD = c.readLine();
            c.close();

            log.info("Loaded configurations from 'configurations.txt");

            BufferedReader s = new BufferedReader(new FileReader("shard_configuration.txt"));
            SHARD_COUNT = Integer.parseInt(s.readLine());
            s.close();

            log.info("Loaded configurations from 'shard_configurations.txt");

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

                log.info(keyFiles.length + " API keys successfully loaded.");
            }

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "loadAPI");
        }
    }

}
