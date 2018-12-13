// Program: Yuuko (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 30/11/2018 - JDK 11.0.0

package com.yuuko.core;

import com.yuuko.core.controllers.GenericGuildController;
import com.yuuko.core.controllers.GenericGuildVoiceController;
import com.yuuko.core.controllers.GenericMessageController;
import com.yuuko.core.controllers.GenericMessageReactionController;
import com.yuuko.core.database.DatabaseConnection;
import com.yuuko.core.modules.C;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.M;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.audio.handlers.AudioManagerManager;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Yuuko extends ListenerAdapter {

    /**
     * Initialises the bot and JDA.
     * @param args -> program arguments (currently unused)
     * @throws LoginException -> If there was an error logging in.
     * @throws IllegalArgumentException -> If a JDA argument was incorrect.
     */
    public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException {
        Configuration.load();
        Configuration.loadApi();
        new DatabaseConnection();

        Cache.JDA = new JDABuilder(AccountType.BOT)
                .useSharding(0, 1)
                .setToken(Configuration.BOT_TOKEN)
                .addEventListener(new Yuuko())
                .setEventManager(new Yuuko.ThreadedEventManager())
                .build();
        Cache.JDA.awaitReady();
        Cache.JDA.getPresence().setGame(Game.of(Game.GameType.LISTENING, Configuration.STATUS));

        Cache.BOT = Cache.JDA.getSelfUser();
        Cache.GUILD_COUNT = Cache.JDA.getGuilds().size();

        if(Configuration.API_KEYS.containsKey("discordbots")) {
            Cache.BOT_LIST = new DiscordBotListAPI.Builder().botId(Cache.BOT.getId()).token(Utils.getApiKey("discordbots")).build();
            Utils.updateDiscordBotList();
        }

    }

    /**
     * Constructor for the class, initialises the UI, the internal clock.
     * Retrieves a list of modules via reflection.
     */
    private Yuuko() {
        // Prints a cool banner :^)
        try {
            String[] args = new String[] {"/bin/bash", "-c", "figlet -c Yuuko " + Configuration.VERSION};
            Process p = new ProcessBuilder(args).start();
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
            ex.printStackTrace();
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

            Cache.STANDARD_STRINGS = new String[3];
            Cache.STANDARD_STRINGS[0] = Configuration.VERSION;
            Cache.STANDARD_STRINGS[1] = Cache.STANDARD_STRINGS[0] + " · Information requested by ";
            Cache.STANDARD_STRINGS[2] = Cache.STANDARD_STRINGS[0] + " · Requested by ";
            Cache.MODULES = moduleList;
            Cache.COMMANDS = commandList;
            Cache.SETTINGS = settingsList;

            Cache.LATEST_INFO = "";
            Cache.LAST_TEN = new LinkedList<>();
            for(int i = 0; i < 10; i++) {
                Cache.LAST_TEN.add("");
            }

            // Start the system clock last to ensure everything else has started.
            new SystemClock();

        } catch(Exception ex) {
            Utils.sendException(ex, "new Yuuko()");
        }
    }

    /**
     * Captures and deals with generic guild events.
     * @param e GenericGuildEvent
     */
    @Override
    public void onGenericGuild(GenericGuildEvent e) {
        try {
            new GenericGuildController(e);
        } catch(Exception ex) {
            Utils.sendException(ex, "public void onGenericGuild(GenericGuildEvent e)");
        }
    }

    /**
     * Captures and deals with generic message events.
     * @param e -> GenericMessageEvent.
     */
    @Override
    public void onGenericMessage(GenericMessageEvent e) {
        try {
            new GenericMessageController(e);
        } catch(Exception ex) {
            Utils.sendException(ex, "public void onGenericMessage(GenericMessageEvent e)");
        }
    }

    /**
     * Captures and deals generic reaction events.
     * @param e -> GenericMessageReactionEvent.
     */
    @Override
    public void onGenericMessageReaction(GenericMessageReactionEvent e) {
        try {
            new GenericMessageReactionController(e);
        } catch(Exception ex) {
            Utils.sendException(ex, "public void onGenericMessageReaction(GenericMessageReactionEvent e)");
        }
    }

    /**
     * Captures and deals with generic voice events.
     * @param e -> GenericGuildVoiceEvent.
     */
    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent e) {
        try {
            new GenericGuildVoiceController(e);
        } catch(Exception ex) {
            Utils.sendException(ex, "public void onGenericGuildVoice(GenericGuildVoiceEvent e)");
        }
    }

    /**
     * Threaded Event Manager Class
     */
    private static class ThreadedEventManager extends InterfacedEventManager {
        private final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

        @Override
        public void handle(Event e) {
            threadPool.submit(() -> super.handle(e));
        }
    }

}


