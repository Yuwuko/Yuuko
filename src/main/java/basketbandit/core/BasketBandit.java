// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 30/05/2018 - JDK 10.0.1

package basketbandit.core;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.C;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.M;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.audio.commands.CommandStop;
import basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
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

    public static JDA bot;
    private static User botUser;

    private static ArrayList<Module> moduleList;
    private static ArrayList<Command> commandList;

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

        botUser = bot.getSelfUser();
        Configuration.GLOBAL_PREFIX = botUser.getAsMention();
    }

    /**
     * Constructor for the class, initialises the UI, the internal clock.
     * Retrieves a list of modules via reflection.
     */
    private BasketBandit() {
        new TimeKeeper();
        new AudioManagerHandler();

        moduleList = new ArrayList<>();
        try {
            Field[] modules = M.class.getDeclaredFields();
            for(Field module : modules) {
                moduleList.add((Module)module.get(Module.class));
            }
            System.out.println("[INFO] " + moduleList.size() + " modules successfully loaded.");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        commandList = new ArrayList<>();
        try {
            Field[] commands = C.class.getDeclaredFields();
            for(Field command : commands) {
                commandList.add((Command)command.get(Command.class));
            }
            System.out.println("[INFO] " + commandList.size() + " commands successfully loaded.");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        Configuration.COMMAND_COUNT = commandList.size() + "";
    }

    /**
     * Deals with what happens when the bot joins a server.
     * @param e -> GuildJoinEvent
     */
    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        new Controller(e, commandList);
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
            if(prefix == null || prefix.equals("")) {
                prefix = Configuration.GLOBAL_PREFIX;
            }

            messageCount++;

            if(user.isBot()
                    || msgRawLower.equals(prefix)
                    || msgRawLower.startsWith(prefix + prefix)
                    || msgRawLower.equals(Configuration.GLOBAL_PREFIX)
                    || msgRawLower.startsWith(Configuration.GLOBAL_PREFIX + Configuration.GLOBAL_PREFIX)) {
                return;
            }

            if(msgRawLower.startsWith(prefix) || msgRawLower.startsWith(Configuration.GLOBAL_PREFIX)) {
                new Controller(e, startExecutionNano, commandList, prefix);
                commandCount++;
                return;
            }

            if(msg.getContentRaw().matches("^[0-9]{1,2}$") || msg.getContentRaw().toLowerCase().equals("cancel")) {
                new Controller(e, startExecutionNano);
            }

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


