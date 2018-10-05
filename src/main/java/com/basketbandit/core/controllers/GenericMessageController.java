package com.basketbandit.core.controllers;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.SystemInformation;
import com.basketbandit.core.SystemVariables;
import com.basketbandit.core.database.DatabaseConnection;
import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.commands.CommandSearch;
import com.basketbandit.core.modules.core.settings.SettingCommandLogging;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.lang.reflect.Constructor;
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

            // Help command (Private MessageHandler) throws null pointer for serverLong (Obviously.)
            String server = e.getGuild().getId();
            String msgRawLower = e.getMessage().getContentRaw().toLowerCase();

            String prefix = new DatabaseFunctions().getServerSetting("commandPrefix", server);
            if(prefix == null || prefix.equals("") || msgRawLower.startsWith(Configuration.GLOBAL_PREFIX)) {
                prefix = Configuration.GLOBAL_PREFIX;
            }

            // Ignores messages that consist of just the prefix or starts with the prefix twice.
            if(msgRawLower.equalsIgnoreCase(prefix)
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
                return;
            }

            // Passes nothing through to the console output so it updates a registered message.
            Utils.incrementEvent(0);

        } catch(NullPointerException ex) {
            // Do nothing, null pointers happen. (Should they though...)
        } catch (Exception ex) {
            Utils.sendException(ex, "private void messageReceivedEvent(MessageReceivedEvent e)");
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
            for(Command c : SystemInformation.getCommandList()) {
                if((inputPrefix + input[0]).equalsIgnoreCase(c.getGlobalName()) || (inputPrefix + input[0]).equalsIgnoreCase(prefix + c.getCommandName())) {
                    String commandModule = c.getCommandModule();
                    moduleDbName = Utils.extractModuleName(commandModule, false, true);
                    clazz = Class.forName(commandModule);
                    constructor = clazz.getConstructor(MessageReceivedEvent.class, String[].class);

                    // Check settings to see if command strings are to be deleted
                    if(new DatabaseFunctions().getServerSetting("deleteExecuted", e.getGuild().getId()).equals("1")) {
                        // Apparently some people aren't giving the bot the permissions they should. This check will let them know.
                        if(!e.getGuild().getMemberById(420682957007880223L).hasPermission(Permission.MESSAGE_MANAGE)) {
                            EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("**MESSAGE_MANAGE**");
                            MessageHandler.sendMessage(e, embed.build());
                            return;
                        } else {
                            e.getMessage().delete().queue();
                            break;
                        }
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
                        EmbedBuilder embed = new EmbedBuilder().setTitle("The _" + input[0] + "_ command is excluded from this channel.");
                        MessageHandler.sendMessage(e, embed.build());
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
                EmbedBuilder embed = new EmbedBuilder().setTitle("The _" + input[0] + "_ command is bound to " + boundChannels.toString() + ".");
                MessageHandler.sendMessage(e, embed.build());
            }

            if(constructor != null && !executed && !bound) {
                constructor.newInstance(e, input);
                executed = true;
            }

            // Print the command and execution time into the console.
            // The main purpose for this is examine where people go wrong when using commands and improve the bot.
            if(executed) {
                executionTime = (System.nanoTime() - startExecutionNano)/1000000;
                Utils.updateLatest(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", " ").replace("Z", "").substring(5) + " - " + e.getGuild().getName() + " - " + e.getMessage().getContentDisplay().toLowerCase() + " (" + executionTime + "ms)");
                Utils.incrementEvent(2);
            }

            if(executed && new DatabaseFunctions().getServerSetting("commandLogging", serverLong).equalsIgnoreCase("1")) {
                new SettingCommandLogging(null, null).executeSetting(e, executionTime);
            }

        } catch (Exception ex) {
            Utils.sendException(ex, "GenericMessageController (Main)");
        }
    }

    /**
     * Deals with non-prefixed commands that are a number between 1-10.
     * @param e MessageReceivedEvent
     * @param startExecutionNano long
     */
    private void processMessage(MessageReceivedEvent e, long startExecutionNano) {
        try {
            if(SystemVariables.searchUsers.containsKey(e.getAuthor().getIdLong())) {
                String[] input = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
                String serverLong = e.getGuild().getId();

                // Search function check if regex matches. Used in conjunction with the search input.
                if(input[0].matches("^[0-9]{1,2}$") || input[0].equals("cancel")) {
                    new CommandSearch().executeCommand(e, input[0]);
                }

                if(new DatabaseFunctions().getServerSetting("deleteExecuted", e.getGuild().getId()).equalsIgnoreCase("true")) {
                    e.getMessage().delete().queue();
                }

                if(new DatabaseFunctions().getServerSetting("commandLogging", serverLong).equalsIgnoreCase("1")) {
                    long executionTime = (System.nanoTime() - startExecutionNano)/1000000;
                    Utils.updateLatest(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", " ").replace("Z", "").substring(5) + " - " + e.getGuild().getName() + " - " + input[0] + " (" + executionTime + "ms)");
                    new SettingCommandLogging(null, null).executeSetting(e, executionTime);
                }

            }

        } catch (Exception ex) {
            Utils.sendException(ex, "GenericMessageController (Aux)");
        }
    }

}
