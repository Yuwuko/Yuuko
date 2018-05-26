// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 24/05/2018 - JDK 10.0.1

package basketbandit.core;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.M;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.audio.handlers.MusicManagerHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BasketBandit extends ListenerAdapter {

    private int messageCount = 0;
    private int commandCount = 0;

    private final Monitor monitor;

    static JDA bot;

    static ArrayList<Module> moduleList;
    static ArrayList<Command> commandList;

    /**
     * Initialises the bot and JDA.
     * @param args -> program arguments (currently unused)
     * @throws LoginException -> If there was an error logging in.
     * @throws IllegalArgumentException -> If a JDA argument was incorrect.
     */
    public static void main(String[] args) throws LoginException, IllegalArgumentException {
        BasketBandit self = new BasketBandit();

        Configuration.PREFIX = args[0];
        Configuration.BOT_ID = args[1];
        Configuration.BOT_TOKEN = args[2];
        Configuration.GOOGLE_API = args[3];
        Configuration.TFL_ID = args[4];
        Configuration.TFL_API = args[5];
        Configuration.DATABASE_URL = args[6];

        bot = new JDABuilder(AccountType.BOT)
            .setToken(Configuration.BOT_TOKEN)
            .addEventListener(self)
            .setEventManager(new ThreadedEventManager())
            .buildAsync();
        bot.getPresence().setGame(Game.of(Game.GameType.DEFAULT,Configuration.STATUS));
    }

    /**
     * Constructor for the class, initialises the UI, the internal clock.
     * Retrieves a list of modules via reflection.
     */
    private BasketBandit() {
        monitor = new Monitor();
        new TimeKeeper(monitor);
        new MusicManagerHandler();

        moduleList = new ArrayList<>();
        try {
            Field[] modules = M.class.getDeclaredFields();
            for(Field module : modules) {
                moduleList.add((Module)module.get(Module.class));
            }
            System.out.println("[INFO] " + moduleList.size() + " module successfully loaded.");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        commandList = new ArrayList<>();
        try {
            Field[] commands = C.class.getDeclaredFields();
            for(Field command : commands) {
                commandList.add((Command)command.get(Command.class));
            }
            System.out.println("[INFO] " + commandList.size() + " modules successfully loaded.");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deals with what happens when the bot joins a server.
     * @param e -> GuildJoinEvent
     */
    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        new Controller(e);
    }

    /**
     * Captures and deals with messages starting with the correct invocation.
     * @param e -> MessageReceivedEvent.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        try {
            Message message = e.getMessage();
            User user = e.getAuthor();

            messageCount++;
            monitor.updateCount(messageCount, commandCount);

            if(user.isBot()
                    || message.getContentRaw().toLowerCase().startsWith(Configuration.PREFIX + Configuration.PREFIX + Configuration.PREFIX)
                    || message.getContentRaw().toLowerCase().equals(Configuration.PREFIX + Configuration.PREFIX)
                    || message.getContentRaw().toLowerCase().equals(Configuration.PREFIX)) {
                return;
            }

            if(message.getContentRaw().startsWith(Configuration.PREFIX + Configuration.PREFIX) || message.getContentRaw().toLowerCase().startsWith(Configuration.PREFIX)) {
                new Controller(e, commandList);
                commandCount++;
                monitor.updateCount(messageCount, commandCount);
            }

            if(message.getContentRaw().matches("^[0-9]{1,2}$") || message.getContentRaw().toLowerCase().equals("cancel")) {
                new Controller(e, commandList);
            }

        } catch(Exception f) {
            f.printStackTrace();
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


