package com.basketbandit.core.controllers;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.database.DatabaseConnection;
import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.ModuleAudio;
import com.basketbandit.core.modules.audio.commands.CommandPlay;
import com.basketbandit.core.modules.logging.ModuleLogging;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class GenericMessageController {

    public GenericMessageController(GenericMessageEvent e) {
        if(e instanceof MessageReceivedEvent) {
            messageReceivedEvent((MessageReceivedEvent)e);
        }
    }

    private void messageReceivedEvent(MessageReceivedEvent e) {
        try {
            // Figure out if the user is a bot or not, so we don't waste any time.
            User user = e.getAuthor();
            if(user.isBot()) {
                return;
            }

            // Used to help calculate execution time of functions.
            long startExecutionNano = System.nanoTime();

            // Help command (Private Message) throws null pointer for serverLong (Obviously.)
            String serverLong = e.getGuild().getId();
            String msgRawLower = e.getMessage().getContentRaw().toLowerCase();

            String prefix = new DatabaseFunctions().getServerPrefix(serverLong);
            if(prefix == null || prefix.equals("") || msgRawLower.startsWith(Configuration.GLOBAL_PREFIX)) {
                prefix = Configuration.GLOBAL_PREFIX;
            }

            if(msgRawLower.equals(prefix)
                    || msgRawLower.startsWith(prefix + prefix)
                    || msgRawLower.equals(Configuration.GLOBAL_PREFIX)
                    || msgRawLower.startsWith(Configuration.GLOBAL_PREFIX + Configuration.GLOBAL_PREFIX)) {
                return;
            }

            if(msgRawLower.startsWith(prefix) || msgRawLower.startsWith(Configuration.GLOBAL_PREFIX)) {
                processMessage(e, startExecutionNano, prefix);
                return;
            }

            if(msgRawLower.matches("^[0-9]{1,2}$") || msgRawLower.equals("cancel")) {
                processMessage(e, startExecutionNano);
            }

            // Passes nothing through to the console output so it updates a registered message.
            Utils.consoleOutput("");

        } catch(NullPointerException ex) {
            // Do nothing, null pointers happen.
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deals with commands that start with a prefix.
     * @param e MessageReceivedEvent
     * @param startExecutionNano long
     * @param prefix String
     */
    private void processMessage(MessageReceivedEvent e, long startExecutionNano, String prefix) {
        String[] input = e.getMessage().getContentRaw().substring(prefix.length()).split("\\s+", 2);
        String inputPrefix = e.getMessage().getContentRaw().substring(0,prefix.length());
        String serverLong = e.getGuild().getId();

        try {
            long executionTime = 0;

            Class<?> clazz;
            Constructor<?> constructor = null;
            String moduleDbName = "";
            String channelId = e.getTextChannel().getId();

            boolean executed = false;
            boolean bound = false;
            StringBuilder boundChannels = new StringBuilder();

            // Iterate through the command list, if the input matches the effective name (includes invocation)
            // find the module class that belongs to the command itself and create a new instance of that
            // constructor (which takes a MessageReceivedEvent) with the parameter of a MessageReceivedEvent.
            // Also return the command's module to check
            for(Command c : Utils.commandList) {
                if((inputPrefix + input[0]).equals(c.getGlobalName()) || (inputPrefix + input[0]).equals(prefix + c.getCommandName())) {
                    String commandModule = c.getCommandModule();
                    moduleDbName = Utils.extractModuleName(commandModule, false, true);
                    clazz = Class.forName(commandModule);
                    constructor = clazz.getConstructor(MessageReceivedEvent.class, String[].class);

                    // Apparently some people aren't giving the bot the permissions they should. This check will let them know.
                    if(!e.getGuild().getMemberById(420682957007880223L).hasPermission(Permission.MESSAGE_MANAGE)) {
                        Utils.sendMessage(e,"Sorry, cannot perform action due to a lack of permission. Missing permission: 'MESSAGE_MANAGE'");
                        return;
                    } else {
                        // Remove the input message.
                        e.getMessage().delete().queue();
                        break;
                    }
                }
            }

            Connection connection = new DatabaseConnection().getConnection();
            ResultSet rs = new DatabaseFunctions().getBindingsExclusionsChannel(connection, serverLong, moduleDbName);

            // While it has next, if excluded is true, if the module name and channel Id match, apologise and break.
            // If excluded is false (implying that bounded is true) and the module name and channel Id do not match,
            // apologise again and break, else execute and set executed to true!
            // If the loop finishes and boolean executed is still false, execute!
            while(rs.next()) {
                if(rs.getBoolean(5)) {
                    if(rs.getString(3).toLowerCase().equals(moduleDbName) && rs.getString(2).equals(channelId)) {
                        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", that command is excluded from this channel.");
                        break;
                    }
                } else {
                    if(rs.getString(3).toLowerCase().equals(moduleDbName) && rs.getString(2).equals(channelId)) {
                        if(constructor != null) {
                            constructor.newInstance(e, input);
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
                boundChannels = Utils.removeLastOccurrence(boundChannels, ", ");
                Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", the " + input[0] + " command is bound to " + boundChannels.toString());
            }

            if(constructor != null && !executed && !bound) {
                constructor.newInstance(e, input);
                executed = true;
            }

            // Print the command and execution time into the console.
            // The main purpose for this is examine where people go wrong when using commands and improve the bot.
            if(executed) {
                executionTime = (System.nanoTime() - startExecutionNano)/1000000;
                Utils.consoleOutput( Instant.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", " ").replace("Z", "").substring(5) + " - " + e.getGuild().getName() + " - " + e.getMessage().getContentDisplay() + " (" + executionTime + "ms)");
            }

            if(executed && new DatabaseFunctions().checkModuleSettings("moduleLogging", serverLong)) {
                new ModuleLogging(e, executionTime, null);
            }

        } catch (InvocationTargetException ex) {
            ex.getCause();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deals with non-prefixed commands that are a number between 1-10.
     * @param e MessageReceivedEvent
     * @param startExecutionNano long
     */
    private void processMessage(MessageReceivedEvent e, long startExecutionNano) {
        try {
            if(ModuleAudio.searchUsers.containsKey(e.getAuthor().getIdLong())) {
                String[] input = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
                String serverLong = e.getGuild().getId();

                // Search function check if regex matches. Used in conjunction with the search input.
                if(input[0].matches("^[0-9]{1,2}$") || input[0].equals("cancel")) {
                    if(!input[0].equals("cancel")) {
                        new CommandPlay(e, ModuleAudio.searchUsers.get(e.getAuthor().getIdLong()).get(Integer.parseInt(input[0]) - 1).getId().getVideoId());
                        ModuleAudio.searchUsers.remove(e.getAuthor().getIdLong());
                    } else if(input[0].equals("cancel")) {
                        ModuleAudio.searchUsers.remove(e.getAuthor().getIdLong());
                        Utils.sendMessage(e, e.getAuthor().getAsMention() + " cancelled their search.");
                    }
                }

                // Remove the input message, but only if searchUsers contains the key.
                e.getMessage().delete().queue();

                if(new DatabaseFunctions().checkModuleSettings("moduleLogging", serverLong)) {
                    long executionTime = (System.nanoTime() - startExecutionNano)/1000000;
                    Utils.consoleOutput(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", " ").replace("Z", "").substring(5) + " - " + e.getGuild().getName() + " - " + input[0] + " (" + executionTime + "ms)");
                    new ModuleLogging(e, executionTime, null);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
