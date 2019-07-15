package com.yuuko.core;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.database.function.BindFunctions;
import com.yuuko.core.database.function.CommandFunctions;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.database.function.ModuleFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import lavalink.client.io.Link;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class CommandExecutor {
    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    private static final List<String> constants = Arrays.asList("core", "developer");
    private static final List<String> disconnectedCommands = Arrays.asList("play", "search", "background", "lyrics");
    private static final List<String> notInVoiceCommands = Arrays.asList("lyrics", "current", "last", "queue");
    private static final List<String> nonDJModeCommands = Arrays.asList("queue", "current", "last", "lyrics");

    private MessageEvent event;
    private Module module;
    private Command command;

    public CommandExecutor(MessageEvent event) {
        // Is the event null? (Runtime setup for module reflection)
        if(event == null) {
            return;
        }

        this.event = event;
        this.module = event.getModule();
        this.command = event.getCommand();

        // Checks to see if the command being executed is a developer command
        // and if it is, is the person executing said command me?
        if(module.getName().equals("developer") && event.getMessage().getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        // Is the module enabled and does the command pass the binding checks?
        // Is module named "audio" and if so, does the user fail any of the checks?
        if(command == null || isDisabled() || isBound() || (module.getName().equals("audio") && !checkAudio())) {
            return;
        }

        // Is the command or module NSFW? If they are, is the channel they're being used in /not/ NSFW?
        if((command.isNSFW() || module.isNSFW()) && !Utilities.isChannelNSFW(event)) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Channel").setDescription("That command can only be used in NSFW marked channels.");
            MessageHandler.sendMessage(event, embed.build());
            return;
        }

        // Does the command have any permissions?
        if(command.getPermissions() != null) {
            // Does the bot have the permission?
            if(!event.getGuild().getMemberById(Configuration.BOT_ID).hasPermission(command.getPermissions())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the '**" + Utilities.getCommandPermissions(command.getPermissions()) + "**' permissions to use that command.");
                MessageHandler.sendMessage(event, embed.build());
                return;
            }

            // Does the user have the permission?
            if(!event.getMember().hasPermission(command.getPermissions()) && !event.getMember().hasPermission(event.getChannel(), command.getPermissions())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("You require the '**" + Utilities.getCommandPermissions(command.getPermissions()) + "**' permissions to use that command.");
                MessageHandler.sendMessage(event, embed.build());
                return;
            }
        }

        // Does the command contain the minimum number of parameters?
        if(!Sanitiser.checkParameters(event, command.getExpectedParameters(), true)) {
            return;
        }

        try {
            log.trace("Invoking {}#onCommand()", command.getClass().getSimpleName());
            command.onCommand(event);
            messageCleanup();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", command.getClass().getSimpleName(), ex.getMessage(), ex);
            event.getMessage().addReaction("❌").queue();
        }
    }

    /**
     * Checks various conditions to see if using certain audio commands are appropriate for the context of the user. Also checks the DJ Mode setting.
     *
     * @return boolean
     */
    private boolean checkAudio() {
        // Is the member /not/ in a voice channel?
        if(!event.getMember().getVoiceState().inVoiceChannel() && !notInVoiceCommands.contains(event.getCommand().getName())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("This command can only be used while in a voice channel.");
            MessageHandler.sendMessage(event, embed.build());
            return false;
        }

        // Is Lavalink /not/ connected and does the command require it to be?
        if(Configuration.LAVALINK.getLavalink().getLink(event.getGuild()).getState() == Link.State.NOT_CONNECTED && !disconnectedCommands.contains(command.getName())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There is no active audio connection.");
            MessageHandler.sendMessage(event, embed.build());
            return false;
        }

        // Is the member an administrator?
        if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            return true;
        }

        // Is DJ mode /not/ on?
        if(!TextUtilities.convertToBoolean(GuildFunctions.getGuildSetting("djMode", event.getGuild().getId()))) {
            return true;
        }

        // Does the member /not/ have the DJ role and if not, is the command a DJ mode command?
        if(event.getMember().getRoles().stream().noneMatch(role -> role.getName().equals("DJ")) && !nonDJModeCommands.contains(command.getName())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("DJ Mode Enabled").setDescription("While DJ mode is active, only a user with the role of 'DJ' can use that command.");
            MessageHandler.sendMessage(event, embed.build());
            return false;
        }

        return true;
    }

    /**
     * Checks if either the module or command has been disabled.
     *
     * @return boolean
     */
    private boolean isDisabled() {
        // Executor still checks core/developer, in this case simply return true.
        if(constants.contains(module.getName())) {
            return false;
        }

        // Checks if the module is disabled.
        if(!ModuleFunctions.isEnabled(event.getGuild().getId(), module.getName())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Module Disabled").setDescription("The `" + module.getName() + "` module is disabled.");
            MessageHandler.sendMessage(event, embed.build());
            return true;
        }

        // Checks if the command is disabled.
        final String guild = event.getGuild().getId();
        final String channel = event.getChannel().getId();
        if(CommandFunctions.isDisabled(guild, "*", "*") ||
                CommandFunctions.isDisabled(guild, channel, "*") ||
                CommandFunctions.isDisabled(guild, "*", command.getName()) ||
                CommandFunctions.isDisabled(guild, channel, command.getName())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Command Disabled").setDescription("The `" + command.getName() + "` command is disabled.");
            MessageHandler.sendMessage(event, embed.build());
            return true;
        }

        return false;
    }

    /**
     * Checks channel bindings to see if commands are allowed to be executed there.
     *
     * @return boolean
     */
    private boolean isBound() {
        if(BindFunctions.checkBind(event.getGuild().getId(), event.getChannel().getId(), module.getName())) {
            return false;
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Module Bound").setDescription("The **" + command.getName() + "** command is bound to **" + BindFunctions.getBindsByModule(event.getGuild(), module.getName(), ", ") + "**.");
            MessageHandler.sendMessage(event, embed.build());
            return true;
        }
    }

    /**
     * Removes the message that issued the command if the `deleteExecuted` setting is toggled to `on`.
     */
    private void messageCleanup() {
        // Does the server want the command message /not/ removed?
        if(!TextUtilities.convertToBoolean(GuildFunctions.getGuildSetting("deleteExecuted", event.getGuild().getId()))) {
            return;
        }

        // Does the bot have permission to remove the message?
        if(event.getGuild().getMemberById(420682957007880223L).hasPermission(Permission.MESSAGE_MANAGE)) { // Can the bot manage messages?
            event.getMessage().delete().queue();
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I am missing the '**MESSAGE_MANAGE**' permission required to execute the 'deleteExecuted' setting. If this setting is active by mistake, use `@Yuuko#2525 setting deleteExecuted false`.");
            MessageHandler.sendMessage(event, embed.build());
        }
    }

}