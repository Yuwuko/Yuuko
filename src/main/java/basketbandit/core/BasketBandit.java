// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 07/05/2018 - JDK 10.0.1

package basketbandit.core;

import basketbandit.core.music.GuildMusicManager;
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
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasketBandit extends ListenerAdapter {

    private int messageCount = 0;
    private int commandCount = 0;

    private UserInterface ui;
    private TimeKeeper tk;
    private HashMap<String, GuildMusicManager> managers;

    /**
     * Initialises the bot and JDA.
     * @param args -> program arguments (currently unused)
     * @throws LoginException -> If there was an error logging in.
     * @throws IllegalArgumentException -> If a JDA argument was incorrect.
     * @throws InterruptedException -> If there was an unexpected interruption.
     * @throws RateLimitedException -> If the bot was rate limited.
     */
    public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException, RateLimitedException {
        BasketBandit self = new BasketBandit();

        new JDABuilder(AccountType.BOT)
                .setToken(Configuration.TOKEN)
                .addEventListener(self)
                .setEventManager(new ThreadedEventManager())
                .buildAsync()
                .getPresence().setGame(Game.of(Game.GameType.DEFAULT,Configuration.STATUS));

    }

    /**
     * Constructor for the class, initialises the UI.
     */
    private BasketBandit() {
        ui = new UserInterface();
        tk = new TimeKeeper(ui);
        managers = new HashMap<>();
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
                new Controller(e, ui, this);
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

    //////////////////////////////////////////////////////////////////// MUSIC

    /**
     * Returns a guild's GuildMusicManager or null if there isn't one.
     * @return trackScheduler or NULL.
     */
    public GuildMusicManager getGuildMusicManager(String guildID) {
        return managers.get(guildID);
    }

    /**
     * Returns full MusicManager HashMap.
     */
    public HashMap<String, GuildMusicManager> getGuildMusicManagers() {
        return managers;
    }

    /**
     * Adds tot the GuildMusicManager HashMap.
     */
    public void addGuildMusicManager(String guildID, GuildMusicManager manager) {
        managers.put(guildID, manager);
    }
}
