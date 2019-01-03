// Program: Yuuko (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 13/12/2018 - JDK 12.0.0

package com.yuuko.core;

import com.yuuko.core.database.DatabaseConnection;
import com.yuuko.core.events.GenericEventManager;
import com.yuuko.core.modules.C;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.M;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.audio.handlers.AudioManagerManager;
import com.yuuko.core.scheduler.ScheduleHandler;
import com.yuuko.core.scheduler.jobs.SecondlyJob;
import com.yuuko.core.scheduler.jobs.ThirtySecondlyJob;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.discordbots.api.client.DiscordBotListAPI;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;

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
            Utils.removeLastOccurrence(output, "\n");
            System.out.println(output);
        } catch(Exception ex) {
            //
        }

        Configuration.load();
        Configuration.loadApi();
        new DatabaseConnection();

        Cache.JDA = new JDABuilder(AccountType.BOT)
                .useSharding(0, 1)
                .setToken(Configuration.BOT_TOKEN)
                .addEventListener(new GenericEventManager())
                .setEventManager(new GenericEventManager.ThreadedEventManager())
                .build();
        Cache.JDA.awaitReady();
        Cache.JDA.getPresence().setGame(Game.of(Game.GameType.LISTENING, Configuration.STATUS));

        Cache.BOT = Cache.JDA.getSelfUser();
        Metrics.GUILD_COUNT = Cache.JDA.getGuilds().size();

        if(Configuration.API_KEYS.containsKey("discordbots")) {
            Cache.BOT_LIST = new DiscordBotListAPI.Builder().botId(Cache.BOT.getId()).token(Utils.getApiKey("discordbots")).build();
            Utils.updateDiscordBotList();
        }

        try {
            Cache.AUDIO_MANAGER_MANAGER = new AudioManagerManager();

            ArrayList<Module> moduleList = new ArrayList<>();
            try {
                M m = new M();
                Field[] modules = m.getClass().getDeclaredFields();
                for(Field module : modules) {
                    moduleList.add((Module) module.get(Module.class));
                }
                System.out.println("[INFO] " + moduleList.size() + " modules successfully loaded.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            ArrayList<Command> commandList = new ArrayList<>();
            try {
                C c = new C();
                Field[] commands = c.getClass().getDeclaredFields();
                for(Field command : commands) {
                    commandList.add((Command) command.get(Command.class));
                }
                System.out.println("[INFO] " + commandList.size() + " commands successfully loaded.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Add them in lowercase so they're easier to compare later.
            ArrayList<String> settingsList = new ArrayList<>();
            settingsList.add("commandprefix");
            settingsList.add("deleteexecuted");
            settingsList.add("commandlogging");
            settingsList.add("nowplaying");
            settingsList.add("djmode");
            settingsList.add("welcomemembers");

            Cache.STANDARD_STRINGS = new String[3];
            Cache.STANDARD_STRINGS[0] = Configuration.VERSION;
            Cache.STANDARD_STRINGS[1] = Cache.STANDARD_STRINGS[0] + " · Information requested by ";
            Cache.STANDARD_STRINGS[2] = Cache.STANDARD_STRINGS[0] + " · Requested by ";
            Cache.MODULES = moduleList;
            Cache.COMMANDS = commandList;
            Cache.SETTINGS = settingsList;

            Cache.LATEST_INFO = "";
            Cache.LAST_THIRTEEN = new LinkedList<>();
            for(int i = 0; i < 13; i++) {
                Cache.LAST_THIRTEEN.add("");
            }

            ScheduleHandler.registerJob(new SecondlyJob());
            ScheduleHandler.registerJob(new ThirtySecondlyJob());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "new Yuuko()");
        }

        new ScheduleHandler();

    }

}


