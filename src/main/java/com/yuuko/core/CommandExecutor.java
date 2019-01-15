package com.yuuko.core;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.database.ModuleBindFunctions;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.modules.Module;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.dv8tion.jda.core.audio.hooks.ConnectionStatus.NOT_CONNECTED;

public class CommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    public CommandExecutor(MessageReceivedEvent e, Module module, String[] cmd) {
        if(e == null) { // Is the event null?
            return;
        }

        if(module.checkModuleSettings(e)) { // Is the module enabled?
            if(module.getName().equals("Audio") && !checkAudio(e, cmd)) { // Is module named Audio? If so, does the user fail any of the checks?
                return;
            }
            if(!module.isChannelNSFW(e) && module.isModuleNSFW()) { // Is the channel NSFW? If not, is the module NSFW?
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Channel").setDescription("That command can only be used in NSFW marked channels.");
                MessageHandler.sendMessage(e, embed.build());
            } else {
                if(checkBinding(e, module, cmd)) {
                    module.getCommandsList().stream().filter(command -> command.getName().equalsIgnoreCase(cmd[0])).findFirst().ifPresent(command -> {
                        if(command.getPermissions() != null && !e.getGuild().getMemberById(Configuration.BOT_ID).hasPermission(command.getPermissions())) { // Is the command permission NULL? If so, does the bot have the permission?
                            EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the '**" + Utils.getCommandPermissions(command.getPermissions()) + "**' permissions to use that command.");
                            MessageHandler.sendMessage(e, embed.build());
                        } else {
                            if(command.getPermissions() != null && !e.getMember().hasPermission(command.getPermissions()) && !e.getMember().hasPermission(e.getTextChannel(), command.getPermissions())) { // Is the command permission NULL? If so, does the user have the permission?
                                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("You require the '**" + Utils.getCommandPermissions(command.getPermissions()) + "**' permissions to use that command.");
                                MessageHandler.sendMessage(e, embed.build());
                            } else {
                                if(Sanitiser.checkParameters(e, cmd, command.getExpectedParameters())) { // Does the command contain the minimum number of parameters?
                                    try {
                                        log.trace("Invoking {}#executeCommand()", command.getClass().getName());
                                        command.executeCommand(e, cmd);
                                        MetricsManager.getEventMetrics().COMMANDS_EXECUTED.getAndIncrement();
                                    } catch (Exception ex) {
                                        log.error("An error occurred while running the {} class, message: {}", command.getClass().getSimpleName(), ex.getMessage(), ex);
                                        MetricsManager.getEventMetrics().COMMANDS_FAILED.getAndIncrement();
                                        MessageHandler.sendException(ex, command.getClass().getSimpleName());
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
        messageCleanup(e);
    }

    /**
     * Removes the message the caused the command to execute if the deleteExecuted setting is toggled to on.
     * @param e MessageReceivedEvent
     */
    private void messageCleanup(MessageReceivedEvent e) {
        if(new DatabaseFunctions().getGuildSetting("deleteExecuted", e.getGuild().getId()).equals("1")) { // Does the server want the command message removed?
            if(!e.getGuild().getMemberById(420682957007880223L).hasPermission(Permission.MESSAGE_MANAGE)) { // Can the bot manage messages?
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I am missing the '**MESSAGE_MANAGE**' permission required to execute the 'deleteExecuted' setting. If this setting is active by mistake, use **'@Yuuko settings deleteExecuted false'** to turn it off.");
                MessageHandler.sendMessage(e, embed.build());
            } else {
                e.getMessage().delete().queue();
            }
        }
    }

    /**
     * Checks various conditions to see if using certain audio commands are appropriate for the context of the user. Also checks the DJ Mode setting.
     * @param e MessageReceivedEvent
     * @param command String[]
     * @return boolean
     */
    private boolean checkAudio(MessageReceivedEvent e, String[] command) {
        if(!e.getMember().getVoiceState().inVoiceChannel()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("This command can only be used while in a voice channel.");
            MessageHandler.sendMessage(e, embed.build());
            return false;
        }

        if(e.getGuild().getAudioManager().getConnectionStatus() == NOT_CONNECTED && !command[0].equals("play") && !command[0].equals("search") && !command[0].equals("background")) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There is no active audio connection.");
            MessageHandler.sendMessage(e, embed.build());
            return false;
        }

        if(!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            if(new DatabaseFunctions().getGuildSetting("djMode", e.getGuild().getId()).equals("1")) {
                if(e.getMember().getRoles().stream().noneMatch(role -> role.getName().equals("DJ"))) {
                    if(!command[0].equals("queue") && !command[0].equals("current") && !command[0].equals("last")) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("DJ Mode Enabled").setDescription("While DJ mode is active, only a user with the role of 'DJ' can use that command.");
                        MessageHandler.sendMessage(e, embed.build());
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Checks channel bindings to see if commands are allowed to be executed there.
     * @param e MessageReceivedEvent
     * @param module Module
     * @return boolean
     */
    private boolean checkBinding(MessageReceivedEvent e, Module module, String[] command) {
        try {
            if(ModuleBindFunctions.checkBind(e.getGuild().getId(), e.getChannel().getId(), module.getName())) {
                return true;
            }

            EmbedBuilder embed = new EmbedBuilder().setTitle("Module Bound").setDescription("The **" + command[0] + "** command is bound to **" + ModuleBindFunctions.getBindsByModule(e.getGuild(), module.getName(), ", ") + "**.");
            MessageHandler.sendMessage(e, embed.build());

            return false;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", command.getClass().getSimpleName(), ex.getMessage(), ex);
            return true;
        }
    }

}