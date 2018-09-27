// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 13/06/2018 - JDK 10.0.1

package com.basketbandit.core;

import com.basketbandit.core.controllers.GenericGuildController;
import com.basketbandit.core.controllers.GenericGuildVoiceController;
import com.basketbandit.core.controllers.GenericMessageController;
import com.basketbandit.core.controllers.GenericMessageReactionController;
import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.M;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BasketBandit extends ListenerAdapter {

    public static JDA bot;

    /**
     * Initialises the bot and JDA.
     * @param args -> program arguments (currently unused)
     * @throws LoginException -> If there was an error logging in.
     * @throws IllegalArgumentException -> If a JDA argument was incorrect.
     */
    public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException, IOException {
        BasketBandit self = new BasketBandit();

        BufferedReader config = new BufferedReader(new FileReader("configuration.txt"));

        Configuration.BOT_ID = config.readLine();
        Configuration.BOT_TOKEN = config.readLine();
        Configuration.GOOGLE_API = config.readLine();
        Configuration.TFL_ID = config.readLine();
        Configuration.TFL_API = config.readLine();
        Configuration.WOW_API = config.readLine();
        Configuration.DATABASE_IP = config.readLine();
        Configuration.DATABASE_NAME = config.readLine();
        Configuration.DATABASE_USERNAME = config.readLine();
        Configuration.DATABASE_PASSWORD = config.readLine();
        Configuration.OSU_API = config.readLine();

        bot = new JDABuilder(AccountType.BOT)
                .useSharding(0, 1)
                .setToken(Configuration.BOT_TOKEN)
                .addEventListener(self)
                .setEventManager(new ThreadedEventManager())
                .build();
        bot.awaitReady();

        bot.getPresence().setGame(Game.of(Game.GameType.LISTENING, Configuration.STATUS));

        Configuration.GLOBAL_PREFIX = bot.getSelfUser().getAsMention() + " ";
        Utils.botUser = bot.getSelfUser();
        Utils.botList = new DiscordBotListAPI.Builder().token(config.readLine()).build();
        Utils.updateDiscordBotList();

        config.close();
    }

    /**
     * Constructor for the class, initialises the UI, the internal clock.
     * Retrieves a list of modules via reflection.
     */
    private BasketBandit() {
        // Prints a cool program banner :^)
        try {
            String[] args = new String[] {"/bin/bash", "-c", "figlet -c BasketBandit " + Configuration.VERSION};
            Process p = new ProcessBuilder(args).start();
            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            StringBuffer output = new StringBuffer();
            while(line != null) {
                output.append(line).append("\n");
                line = reader.readLine();
            }
            output = Utils.removeLastOccurrence(output, "\n");
            System.out.println(output);

        } catch(Exception ex) {
            ex.printStackTrace();

        }

        new TimeKeeper();
        new AudioManagerHandler();

        ArrayList<Module> moduleList = new ArrayList<>();
        try {
            Field[] modules = M.class.getDeclaredFields();
            for(Field module : modules) {
                moduleList.add((Module)module.get(Module.class));
            }
            System.out.println("[INFO] " + moduleList.size() + " modules successfully loaded.");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        ArrayList<Command> commandList = new ArrayList<>();
        try {
            Field[] commands = C.class.getDeclaredFields();
            for(Field command : commands) {
                commandList.add((Command)command.get(Command.class));
            }
            System.out.println("[INFO] " + commandList.size() + " commands successfully loaded.");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        Utils.moduleList = moduleList;
        Utils.commandList = commandList;
        Utils.commandCount = commandList.size() + "";
    }

    /**
     * Captures and deals with generic guild events.
     * @param e GenericGuildEvent
     */
    @Override
    public void onGenericGuild(GenericGuildEvent e) {
        new GenericGuildController(e);
    }

    /**
     * Captures and deals with generic message events.
     * @param e -> GenericMessageEvent.
     */
    @Override
    public void onGenericMessage(GenericMessageEvent e) {
        new GenericMessageController(e);
    }

    /**
     * Captures and deals generic reaction events.
     * @param e -> GenericMessageReactionEvent.
     */
    @Override
    public void onGenericMessageReaction(GenericMessageReactionEvent e) {
        new GenericMessageReactionController(e);
    }

    /**
     * Captures and deals with generic voice events.
     * @param e -> GenericGuildVoiceEvent.
     */
    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent e) {
        new GenericGuildVoiceController(e);
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


