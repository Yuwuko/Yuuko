// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 06/06/2018 - JDK 10.0.1

package com.basketbandit.core;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.M;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.audio.commands.CommandStop;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BasketBandit extends ListenerAdapter {

    public static JDA bot;
    private static User botUser;

    /**
     * Initialises the bot and JDA.
     * @param args -> program arguments (currently unused)
     * @throws LoginException -> If there was an error logging in.
     * @throws IllegalArgumentException -> If a JDA argument was incorrect.
     */
    public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException {
        BasketBandit self = new BasketBandit();

        Configuration.BOT_ID = args[0];
        Configuration.BOT_TOKEN = args[1];
        Configuration.GOOGLE_API = args[2];
        Configuration.TFL_ID = args[3];
        Configuration.TFL_API = args[4];
        Configuration.DATABASE_IP = args[5];
        Configuration.DATABASE_NAME = args[6];
        Configuration.DATABASE_USERNAME = args[7];
        Configuration.DATABASE_PASSWORD = args[8];

        bot = new JDABuilder(AccountType.BOT)
            .setToken(Configuration.BOT_TOKEN)
            .addEventListener(self)
            .setEventManager(new ThreadedEventManager())
            .buildBlocking();
        bot.getPresence().setGame(Game.of(Game.GameType.LISTENING, Configuration.STATUS));

        botUser = bot.getSelfUser();
        Configuration.GLOBAL_PREFIX = botUser.getAsMention() + " ";
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
     * Deals with what happens when the bot joins a server.
     * @param e -> GuildJoinEvent
     */
    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        new Controller(e);
        System.out.println("[INFO] Joined new server: " + e.getGuild().getName() + " (" + e.getGuild().getIdLong() + ")");
    }

    /**
     * Deals with what happens when the bot leaves a server.
     * @param e -> onGuildLeave
     */
    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        new Controller(e);
        System.out.println("[INFO] Left server: " + e.getGuild().getName() + " (" + e.getGuild().getIdLong() + ")");
    }

    /**
     * Captures and deals with messages starting with the correct invocation.
     * @param e -> MessageReceivedEvent.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        try {
            // Used to help calculate execution time of functions.
            long startExecutionNano = System.nanoTime();

            // Help command (Private Message) throws null pointer for serverLong (Obviously.)
            String serverLong = e.getGuild().getId();
            Message msg = e.getMessage();
            String msgRawLower = msg.getContentRaw().toLowerCase();
            User user = e.getAuthor();

            String prefix = new DatabaseFunctions().getServerPrefix(serverLong);
            if(prefix == null || prefix.equals("") || msgRawLower.startsWith(Configuration.GLOBAL_PREFIX)) {
                prefix = Configuration.GLOBAL_PREFIX;
            }

            if(user.isBot()
                    || msgRawLower.equals(prefix)
                    || msgRawLower.startsWith(prefix + prefix)
                    || msgRawLower.equals(Configuration.GLOBAL_PREFIX)
                    || msgRawLower.startsWith(Configuration.GLOBAL_PREFIX + Configuration.GLOBAL_PREFIX)) {
                return;
            }

            if(msgRawLower.startsWith(prefix) || msgRawLower.startsWith(Configuration.GLOBAL_PREFIX)) {
                new Controller(e, startExecutionNano, prefix);
                return;
            }

            if(msgRawLower.matches("^[0-9]{1,2}$") || msg.getContentRaw().toLowerCase().equals("cancel")) {
                new Controller(e, startExecutionNano);
            }

        } catch(NullPointerException ex) {
            // Do nothing, null pointers happen.
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Captures and deals with reacts.
     * @param e -> MessageReactionAddEvent.
     */
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        MessageReaction react = e.getReaction();

        if(react.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            new Controller(e);
        }
    }

    /**
     * Captures and deals with react removals.
     * @param e -> MessageReactionRemoveEvent.
     */
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        MessageReaction react = e.getReaction();

        if(react.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            new Controller(e);
        }
    }

    /**
     * Captures and deals with generic voide events.
     * @param e -> GuildVoiceLeaveEvent.
     */
    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent e) {
        Member self = e.getGuild().getSelfMember();

        if(self.getVoiceState().inVoiceChannel()) {
            if(self.getVoiceState().getChannel().getMembers().size() == 1) {
                new CommandStop(e);
            }
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


