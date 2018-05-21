// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core;

import basketbandit.core.music.MusicManagerHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasketBandit extends ListenerAdapter {

    private int messageCount = 0;
    private int commandCount = 0;

    private UserInterface ui;
    private TimeKeeper tk;
    private Database database;

    /**
     * Initialises the bot and JDA.
     * @param args -> program arguments (currently unused)
     * @throws LoginException -> If there was an error logging in.
     * @throws IllegalArgumentException -> If a JDA argument was incorrect.
     */
    public static void main(String[] args) throws LoginException, IllegalArgumentException {
        BasketBandit self = new BasketBandit();

        new JDABuilder(AccountType.BOT)
                .setToken(args[1])
                .addEventListener(self)
                .setEventManager(new ThreadedEventManager())
                .buildAsync()
                .getPresence().setGame(Game.of(Game.GameType.DEFAULT,Configuration.STATUS));

        Configuration.BOT_ID = args[0];
        Configuration.GOOGLE_API = args[2];
        Configuration.TFL_ID = args[3];
        Configuration.TFL_API = args[4];
    }

    /**
     * Constructor for the class, initialises the UI.
     */
    private BasketBandit() {
        ui = new UserInterface();
        tk = new TimeKeeper(ui);
        database = new Database();
        new MusicManagerHandler();
    }

    /**
     * Captures and deals with messages starting with the correct invocation.
     * @param e -> Message received event.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        try {
            Message message = e.getMessage();
            User user = e.getAuthor();

            messageCount++;
            ui.updateCount(messageCount, commandCount);

            if(user.isBot()
                    || message.getContentRaw().toLowerCase().startsWith(Configuration.PREFIX + Configuration.PREFIX + Configuration.PREFIX)
                    || message.getContentRaw().toLowerCase().equals(Configuration.PREFIX)
                    || message.getContentRaw().toLowerCase().equals(Configuration.PREFIX + Configuration.PREFIX)) {
                return;
            }

            if(message.getContentRaw().startsWith(Configuration.PREFIX + Configuration.PREFIX) || message.getContentRaw().toLowerCase().startsWith(Configuration.PREFIX)) {
                new Controller(e, database);
                commandCount++;
                ui.updateCount(messageCount, commandCount);
            }
        } catch(Exception f) {
            f.printStackTrace();
        }
    }

    /**
     * Captures and deals with reacts.
     * @param e -> React add event.
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
     * @param e -> React remove event.
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


