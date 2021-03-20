// Program: Yuuko (Discord Bot)
// Programmer: Joshua Mark Hunt

package com.yuuko;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import com.yuuko.api.ApiManager;
import com.yuuko.commands.Command;
import com.yuuko.commands.Module;
import com.yuuko.commands.audio.handlers.AudioManager;
import com.yuuko.commands.core.commands.BindCommand;
import com.yuuko.database.connection.DatabaseConnection;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.database.function.ShardFunctions;
import com.yuuko.events.GenericEventManager;
import com.yuuko.metrics.MetricsManager;
import com.yuuko.scheduler.ScheduleHandler;
import com.yuuko.scheduler.jobs.*;
import com.yuuko.utilities.Utilities;
import lavalink.client.io.Link;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.discordbots.api.client.DiscordBotListAPI;
import org.reflections8.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class Yuuko {
    private static final Logger log = LoggerFactory.getLogger(Yuuko.class);
    public static final String VERSION = new SimpleDateFormat("yy'w'w").format(Calendar.getInstance().getTime()) + "b";
    public static String AUTHOR;
    public static String AUTHOR_WEBSITE;
    public static String SUPPORT_GUILD;
    public static long LOG_ERROR;
    public static boolean LOG_METRICS;
    public static String BOT_ID;
    private static String BOT_TOKEN;
    public static int SHARDS_TOTAL = 0;
    public static int SHARDS_INSTANCE = 0;
    public static List<Integer> SHARD_IDS = new ArrayList<>();
    public static String GLOBAL_PREFIX;
    public static ApiManager API_MANAGER;
    public static ShardManager SHARD_MANAGER;
    public static SelfUser BOT;
    public static Map<String, Module> MODULES = new HashMap<>();
    public static Map<String, Command> COMMANDS = new HashMap<>();
    public static final List<String> LOCKED_MODULES = Arrays.asList("core", "setting", "developer");
    public static DiscordBotListAPI BOT_LIST;
    public static final List<String> STANDARD_STRINGS = Arrays.asList(VERSION,
            VERSION + " • Requested by ",
            VERSION + " • Asked by ",
            "Requested by "
    );

    public static void main(String[] args) {
        new Yuuko();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            AudioManager.LAVALINK.getLavalink().getLinks().forEach(Link::destroy);
            SHARD_MANAGER.shutdown();
            SHARD_IDS.forEach(ShardFunctions::updateShardShutdown);
        }));
    }

    public Yuuko() {
        try {
            long loadStart = System.nanoTime();

            // This order DOES matter.
            setupFiles();
            setupDatabase();
            loadConfiguration();
            registerShards();
            setupApi();
            setupCommands();
            setupAudio();
            setupMetrics();
            buildShardManager();
            verifyDatabase();
            setupScheduler();
            setupBotLists();

            log.info("Loading complete... time taken: {} seconds.", (BigDecimal.valueOf((System.nanoTime() - loadStart) / 1000000000.0).setScale(2, RoundingMode.HALF_UP)));
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Yuuko.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Creates and initialises configuration files if they are missing.
     */
    private void setupFiles() {
        try {
            boolean mustEdit = new File("./config").mkdirs();
            new File("./config/api").mkdirs();
            new File("./config/hikari").mkdirs();

            if(new File("./config/config.yaml").createNewFile()) {
                log.warn("Created configuration file: ./config/config.yaml");
                try(FileWriter w = new FileWriter("./config/config.yaml")) {
                    w.write(
                            "author: \"\"" + System.lineSeparator() +
                            "website: \"\"" + System.lineSeparator() +
                            "support: \"\"" + System.lineSeparator() +
                            "log_error: \"\"" + System.lineSeparator() +
                            "log_metrics: \"false\"" + System.lineSeparator() +
                            "bot_id: \"\"" + System.lineSeparator() +
                            "bot_token: \"\"" + System.lineSeparator() +
                            "global_prefix: \"\"" + System.lineSeparator() +
                            "shards_instance: \"1\"" + System.lineSeparator() +
                            "shards_total: \"1\""
                    );
                }
            }

            if(new File("./config/hikari/externaldb.properties").createNewFile()) {
                log.warn("Created configuration file: ./config/hikari/externaldb.properties");
                try(FileWriter w = new FileWriter("./config/hikari/externaldb.properties")) {
                    w.write(
                            "driverClassName=com.mysql.cj.jdbc.Driver" + System.lineSeparator() +
                            "jdbcUrl=jdbc:mysql://localhost/dbyuuko?useSSL=true&serverTimezone=UTC" + System.lineSeparator() +
                            "dataSource.user=" + System.lineSeparator() +
                            "dataSource.password=" + System.lineSeparator() +
                            "dataSource.cachePrepStmts=true" + System.lineSeparator() +
                            "dataSource.prepStmtCacheSize=250" + System.lineSeparator() +
                            "dataSource.prepStmtCacheSqlLimit=2048" + System.lineSeparator() +
                            "dataSource.useServerPrepStmts=true" + System.lineSeparator() +
                            "dataSource.useLocalSessionState=true" + System.lineSeparator() +
                            "dataSource.rewriteBatchedStatements=true" + System.lineSeparator() +
                            "dataSource.cacheResultSetMetadata=true" + System.lineSeparator() +
                            "dataSource.cacheServerConfiguration=true" + System.lineSeparator() +
                            "dataSource.elideSetAutoCommits=true" + System.lineSeparator() +
                            "dataSource.maintainTimeStats=false"
                    );
                }
            }

            if(new File("./config/lavalink.yaml").createNewFile()) {
                log.warn("Created configuration file: ./config/lavalink.yaml");
                try(FileWriter w = new FileWriter("./config/lavalink.yaml")) {
                    w.write(
                            "# add nodes below as necessary, using format:" + System.lineSeparator() +
                            "#" + System.lineSeparator() +
                            "# ---" + System.lineSeparator() +
                            "# address: \"ws://ip:port\"" + System.lineSeparator() +
                            "# password: \"password\""
                    );
                }
            }

            List<String> apiList = Arrays.asList("discordbots", "genius", "github", "google", "newsapi", "openweathermap", "osu", "tesco", "transportforlondon");
            apiList.forEach(api -> {
                try {
                    if(new File("./config/api/" + api + ".yaml").createNewFile()) {
                        log.warn("Created configuration file: ./config/api/{}.yaml", api);
                        try(FileWriter w = new FileWriter("./config/api/" + api + ".yaml")) {
                            w.write(
                                    "!!com.yuuko.core.api.entity.Api" + System.lineSeparator() +
                                            "name: \"" + api + "\"" + System.lineSeparator() +
                                            "applicationId: \"\"" + System.lineSeparator() +
                                            "apiKey: \"\""
                            );
                        }
                    }
                } catch(IOException e) {
                    log.error("An error occurred while running the {} class, message: {}", Yuuko.class.getSimpleName(), e.getMessage(), e);
                }
            });

            if(mustEdit) {
                System.exit(0);
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", Yuuko.class.getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sets up database from properties file. Also handles backup and internal/external usage.
     */
    private void setupDatabase() {
        DatabaseConnection.setupDatabase();
    }

    /**
     * Loads the main configuration file which includes information regarding author, author's website, support guild, the bot's ID and token.
     * Must be done before buildShardManager().
     */
    private void loadConfiguration() {
        try(InputStream inputStream = new FileInputStream("./config/config.yaml")) {
            Map<String, String> config = new Yaml().load(inputStream);
            AUTHOR = config.get("author");
            AUTHOR_WEBSITE = config.get("website");
            SUPPORT_GUILD = config.get("support");
            LOG_ERROR = Long.parseLong(config.get("log_error"));
            LOG_METRICS = Boolean.parseBoolean(config.get("log_metrics"));
            BOT_ID = config.get("bot_id");
            BOT_TOKEN = config.get("bot_token");
            SHARDS_INSTANCE = Integer.parseInt(config.get("shards_instance"));
            SHARDS_TOTAL = Integer.parseInt(config.get("shards_total"));
            GLOBAL_PREFIX = config.get("global_prefix");
        } catch(IOException ex) {
            log.error("An error occurred while running the {} class, message: {}", Yuuko.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Prunes any expired shards, sets both total shard count and shard ID.
     * Must be done before initialiseAudio().
     */
    private void registerShards() {
        ShardFunctions.pruneExpiredShards();
        for(int i = 0; i < SHARDS_INSTANCE; i++) {
            SHARD_IDS.add(ShardFunctions.provideShardId());
        }
        log.info("Shard ID: {}, Total Shards: {}.", SHARD_IDS, SHARDS_TOTAL);
    }

    /**
     * Loads api keys from the api key folder.
     */
    private void setupApi() {
        try {
            API_MANAGER = new ApiManager();
            log.info("Loaded {} API keys - {}", API_MANAGER.size(), API_MANAGER.getNames().toString());
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Yuuko.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Initialises both the commands and the modules encapsulating those commands.
     */
    private void setupCommands() {
        try {
            for(Class<? extends Module> obj : new Reflections("com.yuuko.commands").getSubTypesOf(Module.class)) {
                if(!Modifier.isAbstract(obj.getModifiers())) {
                    Module module = obj.getConstructor().newInstance();
                    MODULES.put(module.getName(), module);
                    COMMANDS.putAll(module.getCommands());
                }
            }
        } catch(InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("There was a problem reflecting modules/commands, message: {}", e.getMessage(), e);
        }

        log.info("Loaded {} modules, containing {} commands.", MODULES.size(), COMMANDS.size());
    }

    /**
     * Initialises Lavalink.
     * MUST be done before buildShardManager(), MUST be done AFTER loadMainConfiguration().
     */
    private void setupAudio() {
        AudioManager.registerSourceManagers();
        log.info("Initialised Lavalink.");
    }

    /**
     * Initialises metrics right away instead of waiting for the scheduler.
     */
    private void setupMetrics() {
        MetricsManager.registerShardedDiscordMetrics(SHARD_IDS.size());
        log.info("Initialised metrics.");
    }

    /**
     * Builds the shard manager and initialises the BOT object.
     * Must be done before synchronizeDatabase().
     */
    private void buildShardManager() {
        try {
            SHARD_MANAGER = DefaultShardManagerBuilder.create(
                    GatewayIntent.GUILD_BANS,
                    GatewayIntent.GUILD_EMOJIS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_VOICE_STATES
            )
                    .setToken(BOT_TOKEN)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
                    .addEventListeners(new GenericEventManager(), AudioManager.LAVALINK.getLavalink())
                    .setAudioSendFactory(new NativeAudioSendFactory())
                    .setVoiceDispatchInterceptor(AudioManager.LAVALINK.getLavalink().getVoiceInterceptor())
                    .setActivity(Activity.of(Activity.ActivityType.LISTENING, "@Yuuko help"))
                    .setShardsTotal(SHARDS_TOTAL)
                    .setShards(SHARD_IDS)
                    .build();

            while(!isConstructed()) {
                Thread.sleep(100); // I want the thread to be blocked
            }

            BOT = Utilities.getSelfUser();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Yuuko.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Synchronizes the database, adding any guilds that added the bot while it was offline.
     */
    private void verifyDatabase() {
        SHARD_MANAGER.getShards().forEach(shard -> shard.getGuildCache().forEach(guild -> {
            GuildFunctions.addOrUpdateGuild(guild);
            BindCommand.DatabaseInterface.verifyBinds(guild);
        }));
        log.info("Database integrity verified with JDA.");
    }

    /**
     * Initialises bot-list objects and then updates them to match the database.
     */
    private void setupBotLists() {
        if(API_MANAGER.containsKey("discordbots")) {
            BOT_LIST = new DiscordBotListAPI.Builder().botId(BOT.getId()).token(API_MANAGER.getApi("discordbots").getKey()).build();
        }
        log.info("Initialised bot lists.");
    }

    /**
     * Initialises scheduler which runs tasks at set intervals.
     */
    private void setupScheduler() {
        ScheduleHandler.registerJob(new TenSecondlyJob());
        ScheduleHandler.registerJob(new ThirtySecondlyJob());
        ScheduleHandler.registerJob(new OneMinutelyJob());
        ScheduleHandler.registerJob(new OneHourlyJob());
        ScheduleHandler.registerJob(new TwelveHourlyJob());

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
