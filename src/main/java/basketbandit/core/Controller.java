package basketbandit.core;

import basketbandit.core.database.DatabaseConnection;
import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.ModuleAudio;
import basketbandit.core.modules.audio.commands.CommandPlay;
import basketbandit.core.modules.core.commands.CommandSetup;
import basketbandit.core.modules.logging.ModuleLogging;
import basketbandit.core.modules.utility.ModuleUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class Controller {

    /**
     * Controller constructor for GuildJoinEvents
     * @param e GuildJoinEvent
     */
    Controller(GuildJoinEvent e, ArrayList<Command> commandList) {
        List<TextChannel> channels = e.getGuild().getTextChannels();
        User bot = e.getGuild().getMemberById(420682957007880223L).getUser();

        int users = 0;
        for(Guild guild : bot.getJDA().getGuilds()) {
            users += guild.getMemberCache().size();
        }

        EmbedBuilder about = new EmbedBuilder()
                .setColor(Color.WHITE)
                .setAuthor(bot.getName() + "#" + bot.getDiscriminator(), null, bot.getAvatarUrl())
                .setDescription("Thanks for inviting me to your server! Below is a little bit of information about myself, and you can access a list of my modules [here](https://github.com/BasketBandit/BasketBandit-Java)!")
                .setThumbnail(bot.getAvatarUrl())
                .addField("Author", "[0x00000000#0001](https://github.com/BasketBandit/)", true)
                .addField("Version", Configuration.VERSION, true)
                .addField("Servers", bot.getJDA().getGuildCache().size()+"", true)
                .addField("Users", users+"", true)
                .addField("Commands", commandList.size()+"", true)
                .addField("Invocation", Configuration.GLOBAL_PREFIX, true)
                .addField("Uptime", TimeKeeper.runtime, true)
                .addField("Ping", bot.getJDA().getPing()+"", true);

        for(TextChannel c: channels) {
            if(c.getName().toLowerCase().equals("general")) {
                c.sendMessage(about.build()).queue();
                break;
            }
        }

        new CommandSetup();

        e.getGuild().getOwner().getUser().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(about.build()).queue());
    }

    /**
     * Controller constructor for GuildLeaveEvent
     * @param e GuildLeaveEvent
     */
    Controller(GuildLeaveEvent e) {
        new DatabaseFunctions().cleanup(e.getGuild().getId());
    }

    /**
     * Controller constructor for MessageReceivedEvents that contain a prefix.
     * @param e MessageReceivedEvent
     */
    Controller(MessageReceivedEvent e, long startExecutionNano, ArrayList<Command> commandList, String prefix) {
        String[] input = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String serverLong = e.getGuild().getId();

        try {
            Class<?> clazz;
            Constructor<?> constructor = null;
            String moduleDbName = "";
            String channelId = e.getTextChannel().getId();

            boolean executed = false;
            boolean bound = false;
            StringBuilder boundChannels = new StringBuilder();

            // Iterate through the command list, if the input matches the effective name (includes invocation)
            // find the module class that beStrings to the command itself and create a new instance of that
            // constructor (which takes a MessageReceivedEvent) with the parameter of a MessageReceivedEvent.
            // Also return the command's module to check
            for(Command c : commandList) {
                if(input[0].equals(c.getGlobalName()) || input[0].equals(prefix + c.getCommandName())) {
                    String commandModule = c.getCommandModule();
                    moduleDbName = commandModule.substring(commandModule.lastIndexOf(".") + 1).toLowerCase();
                    clazz = Class.forName(commandModule);
                    constructor = clazz.getConstructor(MessageReceivedEvent.class, String.class);

                    // Remove the input message.
                    e.getMessage().delete().queue();
                    break;
                }
            }

            Connection connection = new DatabaseConnection().getConnection();
            ResultSet rs = new DatabaseFunctions().getBindingsExclusionsChannel(connection, serverLong, moduleDbName);

            // While it has next, if excluded is true, if the module name and channel Id match, apologise and break.
            // If excluded is false (implying that bounded is true) and the module name and channel Id do not match,
            // apologise again and break, else execute and set executed to true!
            // If the loop finishes and it and boolean executed is still false, execute!
            while(rs.next()) {
                if(rs.getBoolean(5)) {
                    if(rs.getString(3).toLowerCase().equals(moduleDbName) && rs.getString(2).equals(channelId)) {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command is excluded from this channel.").queue();
                        break;
                    }
                } else {
                    if(rs.getString(3).toLowerCase().equals(moduleDbName) && rs.getString(2).equals(channelId)) {
                        if(constructor != null) {
                            constructor.newInstance(e, prefix);
                            executed = true;
                            break;
                        }
                    }
                    boundChannels.append(e.getGuild().getTextChannelById(rs.getString(2)).getName()).append(", ");
                    bound = true;
                }
            }

            connection.close();

            if(bound && !executed) {
                int index = boundChannels.lastIndexOf(", ");
                if(index > -1) {
                    boundChannels.replace(index, index + 1, ".");
                }
                e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the " + input[0] + " command is bound to " + boundChannels.toString()).queue();
            }

            if(constructor != null && !executed && !bound) {
                constructor.newInstance(e, prefix);
                executed = true;
            }

            if(executed && new DatabaseFunctions().checkModuleSettings("moduleLogging", serverLong)) {
                // Print the command into the console.
                long executionTime = (System.nanoTime() - startExecutionNano)/1000000;
                System.out.println("[" + Thread.currentThread().getName() + "] " + Instant.now() + " - " + e.getGuild().getName() + " - " + input[0] + " (" + executionTime + "ms)");

                new ModuleLogging(e, executionTime, "");
            }

        } catch (InvocationTargetException ex) {
            ex.getCause();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Controller constructor for MessageReceivedEvents that do not contain a prefix.
     * @param e MessageReceivedEvent
     */
    Controller(MessageReceivedEvent e, long startExecutionNano) {
        String[] input = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String serverLong = e.getGuild().getId();

        // Remove the input message.
        e.getMessage().delete().queue();

        System.out.println("[" + Thread.currentThread().getName() + "] " + Instant.now() + " - " + e.getGuild().getName() + " - " + input[0]);

        try {
            // Search function check if regex matches. Used in conjunction with the search input.
            if(input[0].matches("^[0-9]{1,2}$") || input[0].equals("cancel")) {
                if(!input[0].equals("cancel") && ModuleAudio.searchUsers.containsKey(e.getAuthor().getIdLong())) {
                    new CommandPlay(e, ModuleAudio.searchUsers.get(e.getAuthor().getIdLong()).get(Integer.parseInt(input[0]) - 1).getId().getVideoId());

                } else if(input[0].equals("cancel") && ModuleAudio.searchUsers.containsKey(e.getAuthor().getIdLong())) {
                    ModuleAudio.searchUsers.remove(e.getAuthor().getIdLong());
                    e.getTextChannel().sendMessage("[" + e.getAuthor().getAsMention() + "] Search cancelled.").queue();

                }
            }

            if(new DatabaseFunctions().checkModuleSettings("moduleLogging", serverLong)) {
                new ModuleLogging(e, startExecutionNano, "");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Controller constructor for MessageReactionAddEvents
     * @param e MessageReactionAddEvent
     */
    Controller(MessageReactionAddEvent e) {
        MessageReaction react = e.getReaction();
        String serverLong = e.getGuild().getId();

        if(react.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            if(new DatabaseFunctions().checkModuleSettings("moduleUtility", serverLong)) {
                new ModuleUtility(e);
            }
        }
    }

    /**
     * Controller constructor for MessageReactionRemoveEvents
     * @param e MessageReactionRemoveEvent
     */
    Controller(MessageReactionRemoveEvent e) {
        MessageReaction react = e.getReaction();
        String serverLong = e.getGuild().getId();

        if(react.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            if(new DatabaseFunctions().checkModuleSettings("moduleUtility", serverLong)) {
                new ModuleUtility(e);
            }
        }
    }
}
