package com.yuuko.core;

import com.basketbandit.ddbl.DivineAPI;
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import com.yuuko.core.api.ApiManager;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.commands.audio.handlers.LavalinkManager;
import com.yuuko.core.database.connection.MetricsDatabaseConnection;
import com.yuuko.core.database.connection.ProvisionDatabaseConnection;
import com.yuuko.core.database.connection.YuukoDatabaseConnection;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.GenericEventManager;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.scheduler.ScheduleHandler;
import com.yuuko.core.scheduler.jobs.OneHourlyJob;
import com.yuuko.core.scheduler.jobs.TenSecondlyJob;
import com.yuuko.core.scheduler.jobs.ThirtySecondlyJob;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.discordbots.api.client.DiscordBotListAPI;
import org.reflections8.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    public static final String VERSION = "201911r1";
    public static String AUTHOR;
    public static String AUTHOR_WEBSITE;
    public static String SUPPORT_GUILD;
    public static String BOT_ID;
    private static String BOT_TOKEN;
    public static int SHARD_COUNT = 0;
    public static int SHARD_ID = 0;
    public static String GLOBAL_PREFIX;
    public static ApiManager API_MANAGER;
    public static LavalinkManager LAVALINK;
    public static ShardManager SHARD_MANAGER;
    public static SelfUser BOT;
    public static Map<String, Command> COMMANDS;
    public static Map<String, Module> MODULES;
    public static final List<String> LOCKED_MODULES = Arrays.asList("core", "setting", "developer");
    public static DiscordBotListAPI BOT_LIST;
    public static DivineAPI DIVINE_API;

    public static final List<String> STANDARD_STRINGS = Arrays.asList(VERSION,
            VERSION + " • Requested by ",
            VERSION + " • Asked by ",
            "Requested by "
    );

    public static boolean LOG_METRICS = true;

    /**
     * Loads all of the bots configurations. (Order DOES matter)
     */
    void setup() {
        try {
            long loadStart = System.nanoTime();

            initialiseCommands();
            loadApi();
            initialiseDatabase();
            registerShards();
            loadMainConfiguration();
            initialiseAudio();
            buildShardManager();
            synchronizeDatabase();
            initialiseMetrics();
            initialiseSchedule();
            initialiseBotLists();

            log.info("Loading complete... time taken: " + (new BigDecimal((System.nanoTime() - loadStart)/1000000000.0).setScale(2, RoundingMode.HALF_UP)) + " seconds.");

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Configuration.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Initialises both the commands and the modules encapsulating those commands.
     */
    private void initialiseCommands() {
        try {
            Reflections reflections = new Reflections("com.yuuko.core.commands");

            Set<Class<? extends Module>> modules = reflections.getSubTypesOf(Module.class);
            Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);

            MODULES = new HashMap<>(modules.size());
            COMMANDS = new HashMap<>(commands.size());

            for(Class<? extends Module> module : modules) {
                if(!Modifier.isAbstract(module.getModifiers())) {
                    Module obj = module.getConstructor().newInstance();
                    MODULES.put(obj.getName(), obj);
                }
            }

            for(Class<? extends Command> command : commands) {
                if(!Modifier.isAbstract(command.getModifiers())) {
                    Command obj = command.getConstructor().newInstance();
                    COMMANDS.put(obj.getName(), obj);
                }
            }

            log.info("Loaded " + MODULES.size() + " modules and " + COMMANDS.size() + " commands.");

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Configuration.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Loads api keys from the api key folder.
     *
     * @return int number of api keys loaded.
     */
    private static void loadApi() {
        try {
            API_MANAGER = new ApiManager();
            log.info("Loaded " + API_MANAGER.size() + " API keys.");

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Configuration.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Initialises the database function.
     * Must be done before registerShards().
     */
    private void initialiseDatabase() {
        new ProvisionDatabaseConnection();
        new YuukoDatabaseConnection();
        new MetricsDatabaseConnection();

        log.info("Initialised database classes.");
    }

    /**
     * Prunes any expired shards, sets both total shard count and shard ID.
     * Must be done before initialiseAudio().
     */
    private void registerShards() {
        DatabaseFunctions.pruneExpiredShards();
        SHARD_COUNT = DatabaseFunctions.getShardCount();
        SHARD_ID = DatabaseFunctions.provideShardId();

        log.info("Shard ID: " + SHARD_ID + ", Total Shards: " + SHARD_COUNT + ".");
    }

    /**
     * Loads the main configuration file which includes information regarding author, author's website, support guild, the bot's ID and token.
     * Must be done before buildShardManager().
     */
    private void loadMainConfiguration() {
        try(BufferedReader c = new BufferedReader(new FileReader("./config/configuration.txt"))) {
            AUTHOR = c.readLine();
            AUTHOR_WEBSITE = c.readLine();
            SUPPORT_GUILD = c.readLine();
            BOT_ID = c.readLine();
            BOT_TOKEN = c.readLine();
            GLOBAL_PREFIX = "<@" + BOT_ID + "> ";
        } catch(IOException ex) {
            log.error("An error occurred while running the {} class, message: {}", Configuration.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Initialises Lavalink and sets up the AudioManagerController.
     * MUST be done before buildShardManager(), MUST be done AFTER loadMainConfiguration().
     */
    private void initialiseAudio() {
        LAVALINK = new LavalinkManager();
        new AudioManagerController();

        log.info("Initialised Lavalink and AudioManagerController.");
    }

    /**
     * Builds the shard manager and initialises the BOT object.
     * Must be done before synchronizeDatabase().
     */
    private void buildShardManager() {
        try {
            SHARD_MANAGER = new DefaultShardManagerBuilder()
                    .setToken(BOT_TOKEN)
                    .addEventListeners(new GenericEventManager(), LAVALINK.getLavalink())
                    .setAudioSendFactory(new NativeAudioSendFactory())
                    .setVoiceDispatchInterceptor(LAVALINK.getLavalink().getVoiceInterceptor())
                    .setActivity(Activity.of(Activity.ActivityType.LISTENING, "@Yuuko help"))
                    .setShardsTotal(SHARD_COUNT)
                    .setShards(SHARD_ID)
                    .build();

            while(!isConstructed()) {
                Thread.sleep(250);
            }

            BOT = Utilities.getSelfUser();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Configuration.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Synchronizes the database, adding any guilds that added the bot while it was offline.
     */
    private void synchronizeDatabase() {
        GuildFunctions.addGuilds(BOT.getJDA());

        log.info("Synchronised database with JDA.");
    }

    /**
     * Initialises Discord metrics right away instead of waiting for the scheduler.
     */
    private void initialiseMetrics() {
        MetricsManager.truncateMetrics(SHARD_ID);
        MetricsManager.getDiscordMetrics().update();

        log.info("Initialised metrics.");
    }

    /**
     * Initialises bot list objects and then updates them to match the database.
     */
    private void initialiseBotLists() {
        if(API_MANAGER.containsKey("discordbots")) {
            BOT_LIST = new DiscordBotListAPI.Builder().botId(BOT.getId()).token(Utilities.getApiKey("discordbots")).build();
        }
        if(API_MANAGER.containsKey("divinediscordbots")) {
            DIVINE_API = new DivineAPI.Builder().botId(BOT.getId()).token(Utilities.getApiKey("divinediscordbots")).build();
        }
        Utilities.updateDiscordBotList();

        log.info("Initialised bot lists.");
    }

    /**
     * Initialises the bot's scheduler which runs tasks at set intervals.
     */
    private void initialiseSchedule() {
        ScheduleHandler.registerJob(new TenSecondlyJob());
        ScheduleHandler.registerJob(new ThirtySecondlyJob());
        ScheduleHandler.registerJob(new OneHourlyJob());

        log.info("Initialised scheduler.");
    }

    /**
     * Checks to see if all shards are connected.
     *
     * @return boolean
     */
    private boolean isConstructed() {
        if(SHARD_MANAGER == null) {
            return false;
        }

        for(JDA shard : SHARD_MANAGER.getShards()) {
            if(shard.getStatus() != JDA.Status.CONNECTED) {
                return false;
            }
        }
        return true;
    }

}
