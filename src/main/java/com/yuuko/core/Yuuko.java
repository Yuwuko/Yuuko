// Program: Yuuko (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 12/01/2019 - JDK 12.0.0

package com.yuuko.core;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.database.connections.MetricsDatabaseConnection;
import com.yuuko.core.database.connections.SettingsDatabaseConnection;
import com.yuuko.core.events.GenericEventManager;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.ScheduleHandler;
import com.yuuko.core.scheduler.jobs.FiveSecondlyJob;
import com.yuuko.core.scheduler.jobs.ThirtySecondlyJob;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.TextUtility;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.discordbots.api.client.DiscordBotListAPI;
import org.reflections.Reflections;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Yuuko {

    /**
     * Initialises the bot and JDA.
     * @param args -> program arguments (currently unused)
     * @throws LoginException -> If there was an error logging in.
     * @throws IllegalArgumentException -> If a JDA argument was incorrect.
     */
    public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException {
        try {
            // Prints a cool banner :^)
            String[] arguments = new String[] {"/bin/bash", "-c", "figlet -c Yuuko"};
            Process p = new ProcessBuilder(arguments).start();
            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            StringBuffer output = new StringBuffer();
            while(line != null) {
                output.append(line).append("\n");
                line = reader.readLine();
            }
            TextUtility.removeLastOccurrence(output, "\n");
            System.out.println(output);
        } catch(Exception ex) {
            //
        }

        Configuration.load();
        Configuration.loadApi();
        new SettingsDatabaseConnection();
        new MetricsDatabaseConnection();
        DatabaseFunctions.truncateMetrics();

        Cache.JDA = new JDABuilder(AccountType.BOT)
                .useSharding(0, 1)
                .setToken(Configuration.BOT_TOKEN)
                .addEventListener(new GenericEventManager())
                .setEventManager(new GenericEventManager.ThreadedEventManager())
                .build();
        Cache.JDA.awaitReady();
        Cache.JDA.getPresence().setGame(Game.of(Game.GameType.LISTENING, Configuration.STATUS));

        Cache.BOT = Cache.JDA.getSelfUser();
        Configuration.GLOBAL_PREFIX = "<@" + Cache.BOT.getIdLong() + "> ";
        MetricsManager.updateDiscordMetrics();

        if(Configuration.API_KEYS.containsKey("discordbots")) {
            Cache.BOT_LIST = new DiscordBotListAPI.Builder().botId(Cache.BOT.getId()).token(Utils.getApiKey("discordbots")).build();
            Utils.updateDiscordBotList();
        }

        try {
            Reflections reflections = new Reflections("com.yuuko.core.commands");

            Set<Class<? extends Module>> modules = reflections.getSubTypesOf(Module.class);
            List<Module> moduleList = new ArrayList<>();
            for(Class<? extends Module> module: modules) {
                Module obj = module.getConstructor(MessageReceivedEvent.class, String[].class).newInstance(null, null);
                moduleList.add(obj);
            }
            System.out.println("[INFO] " + moduleList.size() + " commands successfully loaded.");

            Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);
            List<Command> commandList = new ArrayList<>();
            for(Class<? extends Command> command: commands) {
                Command obj = command.getConstructor().newInstance();
                commandList.add(obj);
            }
            System.out.println("[INFO] " + commandList.size() + " commands successfully loaded.");

            Cache.AUDIO_MANAGER_MANAGER = new AudioManagerManager();
            Cache.STANDARD_STRINGS = new String[2];
            Cache.STANDARD_STRINGS[0] = Configuration.VERSION;
            Cache.STANDARD_STRINGS[1] = Cache.STANDARD_STRINGS[0] + " · Requested by ";
            Cache.MODULES = moduleList;
            Cache.COMMANDS = commandList;

            ScheduleHandler.registerJob(new FiveSecondlyJob());
            ScheduleHandler.registerJob(new ThirtySecondlyJob());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "new Yuuko()");
        }
    }

}


