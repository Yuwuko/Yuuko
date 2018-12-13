package com.yuuko.core.controllers;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.Statistics;
import com.yuuko.core.database.DatabaseConnection;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.audio.ModuleAudio;
import com.yuuko.core.modules.audio.commands.CommandSearch;
import com.yuuko.core.modules.core.settings.SettingExecuteBoolean;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
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
            // Increment message counter, regardless of it's author.
            Statistics.MESSAGES_PROCESSED.getAndIncrement();

            // Figure out if the user is a bot or not, so we don't waste any time.
            User user = e.getAuthor();
            if(user.isBot()) {
                return;
            }

            // Used to help calculate execution time of functions.
            long startExecutionNano = System.nanoTime();

            String msgRawLower = e.getMessage().getContentRaw().toLowerCase();

            String prefix = Utils.getServerPrefix(e.getGuild().getId());
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
            }

        } catch(NullPointerException ex) {
            // Do nothing, null pointers happen. (Should they though...)
        } catch (Exception ex) {
            MessageHandler.sendException(ex, "private void messageReceivedEvent(MessageReceivedEvent e)");
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
        String inputPrefix = e.getMessage().getContentRaw().substring(0, prefix.length());
        String serverId = e.getGuild().getId();

        try {
            long executionTime;

            Class<?> clazz;
            Constructor<?> constructor = null;
            String moduleDbName = "";

            // Iterate through the command list, if the input matches the effective name (includes invocation)
            // find the module class that belongs to the command itself and create a new instance of that
            // constructor (which takes a MessageReceivedEvent) with the parameter of a MessageReceivedEvent.
            // Also return the command's module to check
            for(Command c : Cache.COMMANDS) {
                if((inputPrefix + input[0]).equalsIgnoreCase(c.getGlobalName()) || (inputPrefix + input[0]).equalsIgnoreCase(prefix + c.getCommandName())) {
                    String commandModule = c.getCommandModule();
                    moduleDbName = Utils.extractModuleName(commandModule, false, true);
                    clazz = Class.forName(commandModule);
                    constructor = clazz.getConstructor(MessageReceivedEvent.class, String[].class);
                }
            }

            boolean isAllowed = checkBindings(e, moduleDbName, input[0]);

            if(constructor != null && isAllowed) {
                constructor.newInstance(e, input);

                executionTime = (System.nanoTime() - startExecutionNano)/1000000;
                Utils.updateLatest(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", " ").replace("Z", "").substring(5) + " - " + e.getGuild().getName() + " - " + e.getMessage().getContentDisplay().toLowerCase() + " (" + executionTime + "ms)");
                Statistics.COMMANDS_PROCESSED.getAndIncrement();

                if(new DatabaseFunctions().getServerSetting("commandLogging", serverId).equalsIgnoreCase("1")) {
                    new SettingExecuteBoolean(null, null, null).executeLogging(e, executionTime);
                }
            }

        } catch (Exception ex) {
            MessageHandler.sendException(ex, "GenericMessageController (Main) - " + e.getMessage().getContentRaw());
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
                String server = e.getGuild().getId();

                // Search function check if regex matches. Used in conjunction with the search input.
                if(input[0].matches("^[0-9]{1,2}$") || input[0].equals("cancel")) {
                    new CommandSearch().executeCommand(e, input[0]);
                }

                if(new DatabaseFunctions().getServerSetting("deleteExecuted", server).equalsIgnoreCase("1")) {
                    e.getMessage().delete().queue();
                }

                if(new DatabaseFunctions().getServerSetting("commandLogging", server).equalsIgnoreCase("1")) {
                    long executionTime = (System.nanoTime() - startExecutionNano)/1000000;
                    Utils.updateLatest(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", " ").replace("Z", "").substring(5) + " - " + e.getGuild().getName() + " - " + input[0] + " (" + executionTime + "ms)");
                    new SettingExecuteBoolean(null, null, null).executeLogging(e, executionTime);
                }

            }

        } catch (Exception ex) {
            MessageHandler.sendException(ex, "GenericMessageController (Aux) - " + e.getMessage().getContentRaw());
        }
    }

    /**
     * Checks channel bindings to see if commands are allowed to be executed there.
     * @param e MessageReceivedEvent
     * @param moduleDbName String
     * @return boolean
     */
    private boolean checkBindings(MessageReceivedEvent e, String moduleDbName, String command) {
        try {
            StringBuilder boundChannels = new StringBuilder();

            Connection connection = DatabaseConnection.getConnection();
            ResultSet rs = new DatabaseFunctions().getBindingsByModule(connection, e.getGuild().getId(), moduleDbName);

            while(rs.next()) {
                if(rs.getString(2).equals(e.getTextChannel().getId()) && rs.getString(3).toLowerCase().equals(moduleDbName)) {
                    connection.close();
                    return true;
                }
                boundChannels.append(e.getGuild().getTextChannelById(rs.getString(2)).getName()).append(", ");
            }
            connection.close();

            if(boundChannels.toString().equals("")) {
                return true;
            } else {
                Utils.removeLastOccurrence(boundChannels, ", ");

                EmbedBuilder embed = new EmbedBuilder().setTitle("The **_" + command + "_** command is bound to " + boundChannels.toString() + ".");
                MessageHandler.sendMessage(e, embed.build());
                return false;
            }

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Bindings");
            return true;
        }
    }

}
