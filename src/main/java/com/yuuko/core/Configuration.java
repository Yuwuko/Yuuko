package com.yuuko.core;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.LavalinkManager;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.database.connections.MetricsDatabaseConnection;
import com.yuuko.core.database.connections.SettingsDatabaseConnection;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.discordbots.api.client.DiscordBotListAPI;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    public static final String VERSION = "17-03-2019_1";
    public static String AUTHOR;
    public static String AUTHOR_WEBSITE;
    public static String SUPPORT_GUILD;
    public static String BOT_ID;
    static String BOT_TOKEN;
    public static int SHARD_COUNT = 0;
    public static int SHARD_ID = 0;
    public static String GLOBAL_PREFIX;
    static String STATUS = "@Yuuko help";
    public static HashMap<String, ApplicationProgrammingInterface> API_KEYS;
    public static LavalinkManager LAVALINK;
    public static net.dv8tion.jda.bot.sharding.ShardManager SHARD_MANAGER;
    public static User BOT;
    public static List<Command> COMMANDS;
    public static List<Module> MODULES;
    public static DiscordBotListAPI BOT_LIST;
    public static String[] STANDARD_STRINGS;
    static AudioManagerManager AUDIO_MANAGER_MANAGER;

    /**
     * Loads all of the bots configurations.
     */
    static void load() {
        try {
            log.info("Loading configurations from 'configurations.txt'...");
            BufferedReader c = new BufferedReader(new FileReader("configuration.txt"));
            AUTHOR = c.readLine();
            AUTHOR_WEBSITE = c.readLine();
            SUPPORT_GUILD = c.readLine();
            BOT_ID = c.readLine();
            BOT_TOKEN = c.readLine();
            c.close();
            log.info("Loaded configurations from 'configurations.txt'.");

            log.info("Loading configurations from 'shard_configurations.txt'...");
            BufferedReader s = new BufferedReader(new FileReader("shard_configuration.txt"));
            SHARD_COUNT = Integer.parseInt(s.readLine());
            s.close();
            log.info("Loaded configurations from 'shard_configurations.txt'.");

            Configuration.loadApi();

            log.info("Setting up settings database connection...");
            new SettingsDatabaseConnection();

            log.info("Setting up metrics database connection...");
            new MetricsDatabaseConnection();

            log.info("Truncating metrics database...");
            DatabaseFunctions.truncateMetrics();

            Reflections reflections = new Reflections("com.yuuko.core.commands");

            log.info("Loading modules...");
            Set<Class<? extends Module>> modules = reflections.getSubTypesOf(Module.class);
            MODULES = new ArrayList<>();
            for(Class<? extends Module> module: modules) {
                Module obj = module.getConstructor(MessageReceivedEvent.class, String[].class).newInstance(null, null);
                MODULES.add(obj);
            }
            log.info("Loaded " + MODULES.size() + " modules successfully.");

            log.info("Loading commands...");
            Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);
            COMMANDS = new ArrayList<>();
            for(Class<? extends Command> command: commands) {
                Command obj = command.getConstructor().newInstance();
                COMMANDS.add(obj);
            }
            log.info("Loaded " + COMMANDS.size() + " commands successfully.");

            log.info("Setting up Lavalink manager...");
            LAVALINK = new LavalinkManager();

            log.info("Setting up AudioManager manager...");
            AUDIO_MANAGER_MANAGER = new AudioManagerManager();

            log.info("Setting up standard strings...");
            STANDARD_STRINGS = new String[]{
                    VERSION,
                    VERSION + " • Requested by ",
                    VERSION + " • Asked by "
            };

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Configuration.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Loads api keys from the api key folder. This is done separately so keys can be reloaded live without reloading the whole configuration.
     */
    public static void loadApi() {
        try {
            File folder = new File("./api/");
            File[] keyFiles = folder.listFiles();

            if(keyFiles != null) {
                log.info("Loading API keys...");
                API_KEYS = new HashMap<>();
                for(File key : keyFiles) {
                    BufferedReader c = new BufferedReader(new FileReader(key));
                    API_KEYS.put(key.getName(), new ApplicationProgrammingInterface(key.getName(), c.readLine(), c.readLine()));
                    c.close();
                }
                log.info("Loaded " + keyFiles.length + " API keys.");
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Configuration.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

}
