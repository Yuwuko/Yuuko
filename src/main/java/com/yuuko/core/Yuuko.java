// Program: Yuuko (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 12/01/2019 - JDK 12.0.0

package com.yuuko.core;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.LavalinkManager;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.database.connections.MetricsDatabaseConnection;
import com.yuuko.core.database.connections.SettingsDatabaseConnection;
import com.yuuko.core.events.GenericEventManager;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.ScheduleHandler;
import com.yuuko.core.scheduler.jobs.FiveSecondlyJob;
import com.yuuko.core.scheduler.jobs.ThirtySecondlyJob;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.discordbots.api.client.DiscordBotListAPI;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Yuuko {

    private static final Logger log = LoggerFactory.getLogger(Yuuko.class);

    /**
     * Initialises the bot and JDA.
     * @param args -> program arguments (currently unused)
     * @throws LoginException -> If there was an error logging in.
     * @throws IllegalArgumentException -> If a JDA argument was incorrect.
     */
    public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException {
        Configuration.load();
        Configuration.loadApi();
        new SettingsDatabaseConnection();
        new MetricsDatabaseConnection();
        DatabaseFunctions.truncateMetrics();
        Configuration.SHARD_ID = Integer.parseInt(args[0]);

        // Lavalink client node setup.
        Cache.LAVALINK = new LavalinkManager();

        Cache.SHARD_MANAGER = new DefaultShardManagerBuilder()
                .setToken(Configuration.BOT_TOKEN)
                .addEventListeners(new GenericEventManager(), Cache.LAVALINK.getLavalink())
                .setAudioSendFactory(new NativeAudioSendFactory())
                .setGame(Game.of(Game.GameType.LISTENING, Configuration.STATUS))
                .setShardsTotal(Configuration.SHARD_COUNT)
                .setShards(Configuration.SHARD_ID)
                .build();

        while(!areShardsBuilt()) {
            log.info("Still waiting...");
            Thread.sleep(2500);
        }

        log.info("Active Shards: " + Cache.SHARD_MANAGER.getShards().size());

        Cache.BOT = Utils.getSelfUser();
        Configuration.GLOBAL_PREFIX = "<@" + Cache.BOT.getId() + "> ";
        MetricsManager.updateDiscordMetrics();

        try {
            Reflections reflections = new Reflections("com.yuuko.core.commands");

            Set<Class<? extends Module>> modules = reflections.getSubTypesOf(Module.class);
            List<Module> moduleList = new ArrayList<>();
            for(Class<? extends Module> module: modules) {
                Module obj = module.getConstructor(MessageReceivedEvent.class, String[].class).newInstance(null, null);
                moduleList.add(obj);
            }
            log.info(moduleList.size() + " commands successfully loaded.");

            Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);
            List<Command> commandList = new ArrayList<>();
            for(Class<? extends Command> command: commands) {
                Command obj = command.getConstructor().newInstance();
                commandList.add(obj);
            }
            log.info(commandList.size() + " commands successfully loaded.");

            Cache.AUDIO_MANAGER_MANAGER = new AudioManagerManager();
            Cache.STANDARD_STRINGS = new String[2];
            Cache.STANDARD_STRINGS[0] = Configuration.VERSION;
            Cache.STANDARD_STRINGS[1] = Cache.STANDARD_STRINGS[0] + " · Requested by ";
            Cache.MODULES = moduleList;
            Cache.COMMANDS = commandList;

            if(Configuration.API_KEYS.containsKey("discordbots")) {
                Cache.BOT_LIST = new DiscordBotListAPI.Builder().botId(Cache.BOT.getId()).token(Utils.getApiKey("discordbots")).build();
                Utils.updateDiscordBotList();
            }

            ScheduleHandler.registerJob(new FiveSecondlyJob());
            ScheduleHandler.registerJob(new ThirtySecondlyJob());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "new Yuuko()");
        }
    }

    private static boolean areShardsBuilt() {
        if(Cache.SHARD_MANAGER == null) {
            return false;
        }

        for(JDA shard : Cache.SHARD_MANAGER.getShards()) {
            if(shard.getStatus() != JDA.Status.CONNECTED) {
                return false;
            }
        }
        return true;
    }

}


