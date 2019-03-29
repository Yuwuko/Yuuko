package com.yuuko.core;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.database.BindFunctions;
import com.yuuko.core.database.GuildFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import lavalink.client.io.Link;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CommandExecutor {
    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    private static final String[] disconnectedCommands = new String[]{"play", "search", "background"};
    private static final String[] nonDJModeCommands = new String[]{"queue", "current", "last"};

    public CommandExecutor(MessageEvent e, Module module) {
        Command command = null;

        // Is the event null?
        if(e == null) {
            return;
        }

        for(Command comm: module.getCommandsAsList()) {
            if(comm.getName().equalsIgnoreCase(e.getCommand()[0])) {
                command = comm;
                break;
            }
        }

        // Is the module enabled and does the command pass the binding checks?
        // Is module named "audio" and if so, does the user fail any of the checks?
        if(command == null || !command.isEnabled(e) || !module.isEnabled(e) || !checkBinding(e, module, e.getCommand()) || (module.getName().equals("Audio") && !checkAudio(e))) {
            return;
        }

        // Is the command or module NSFW? If they are, is the channel they're being used in /not/ NSFW?
        if((command.isNSFW() || module.isNSFW()) && !Utilities.isChannelNSFW(e)) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Channel").setDescription("That command can only be used in NSFW marked channels.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // Does the command have any permissions?
        if(command.getPermissions() != null) {
            // Does the bot have the permission?
            if(!e.getGuild().getMemberById(Configuration.BOT_ID).hasPermission(command.getPermissions())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the '**" + Utilities.getCommandPermissions(command.getPermissions()) + "**' permissions to use that command.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            // Does the user have the permission?
            if(!e.getMember().hasPermission(command.getPermissions()) && !e.getMember().hasPermission(e.getTextChannel(), command.getPermissions())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("You require the '**" + Utilities.getCommandPermissions(command.getPermissions()) + "**' permissions to use that command.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }
        }

        // Does the command contain the minimum number of parameters?
        if(!Sanitiser.checkParameters(e, command.getExpectedParameters(), true)) {
            return;
        }

        try {
            log.trace("Invoking {}#onCommand()", command.getClass().getName());
            command.onCommand(e);
            messageCleanup(e);
            MetricsManager.getEventMetrics().COMMANDS_EXECUTED.getAndIncrement();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", command.getClass().getSimpleName(), ex.getMessage(), ex);
            e.getMessage().addReaction("❌").queue();
            MetricsManager.getEventMetrics().COMMANDS_FAILED.getAndIncrement();
        }
    }

    /**
     * Removes the message the caused the command to execute if the deleteExecuted setting is toggled to on.
     *
     * @param e MessageReceivedEvent
     */
    private void messageCleanup(MessageReceivedEvent e) {
        // Does the server want the command message /not/ removed?
        if(!TextUtilities.convertToBoolean(GuildFunctions.getGuildSetting("deleteExecuted", e.getGuild().getId()))) {
            return;
        }

        // Does the bot have permission to remove the message?
        if(e.getGuild().getMemberById(420682957007880223L).hasPermission(Permission.MESSAGE_MANAGE)) { // Can the bot manage messages?
            e.getMessage().delete().queue();
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I am missing the '**MESSAGE_MANAGE**' permission required to execute the 'deleteExecuted' setting. If this setting is active by mistake, use `@Yuuko#2525 setting deleteExecuted false`.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

    /**
     * Checks various conditions to see if using certain audio commands are appropriate for the context of the user. Also checks the DJ Mode setting.
     *
     * @param e MessageReceivedEvent
     * @return boolean
     */
    private boolean checkAudio(MessageEvent e) {
        // Is the member /not/ in a voice channel?
        if(!e.getMember().getVoiceState().inVoiceChannel()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("This command can only be used while in a voice channel.");
            MessageHandler.sendMessage(e, embed.build());
            return false;
        }

        // Is Lavalink /not/ connected and does the command require it to be?
        if(Configuration.LAVALINK.getLavalink().getLink(e.getGuild()).getState() == Link.State.NOT_CONNECTED && !Arrays.asList(disconnectedCommands).contains(e.getCommand()[0])) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There is no active audio connection.");
            MessageHandler.sendMessage(e, embed.build());

            return false;
        }

        // Is the member an administrator?
        if(e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            return true;
        }

        // Is DJ mode /not/ on?
        if(!TextUtilities.convertToBoolean(GuildFunctions.getGuildSetting("djMode", e.getGuild().getId()))) {
            return true;
        }

        // Does the member /not/ have the DJ role and if not, is the command a DJ mode command?
        if(e.getMember().getRoles().stream().noneMatch(role -> role.getName().equals("DJ")) && !Arrays.asList(nonDJModeCommands).contains(e.getCommand()[0])) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("DJ Mode Enabled").setDescription("While DJ mode is active, only a user with the role of 'DJ' can use that command.");
            MessageHandler.sendMessage(e, embed.build());
            return false;
        }

        return true;
    }

    /**
     * Checks channel bindings to see if commands are allowed to be executed there.
     *
     * @param e MessageReceivedEvent
     * @param module Module
     * @return boolean
     */
    private boolean checkBinding(MessageReceivedEvent e, Module module, String[] command) {
        try {
            // Does the bind check pass?
            if(BindFunctions.checkBind(e.getGuild().getId(), e.getChannel().getId(), module.getName())) {
                return true;
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Module Bound").setDescription("The **" + command[0] + "** command is bound to **" + BindFunctions.getBindsByModule(e.getGuild(), module.getName(), ", ") + "**.");
                MessageHandler.sendMessage(e, embed.build());
                return false;
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", command.getClass().getSimpleName(), ex.getMessage(), ex);
            return true; // Would rather allow commands everywhere than nowhere if bindings broke.
        }
    }

}