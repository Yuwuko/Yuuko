package com.yuuko;

import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.Module;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.core.commands.BindCommand;
import com.yuuko.modules.core.commands.ModuleCommand;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CommandExecutor {
    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    private static final List<String> constants = Arrays.asList("core", "developer", "setting");
    private static final List<String> disconnectedCommands = Arrays.asList("play", "playnext", "search", "background", "lyrics");
    private static final List<String> notInVoiceCommands = Arrays.asList("lyrics", "current", "last", "queue");
    private static final List<String> nonDJModeCommands = Arrays.asList("queue", "current", "last", "lyrics");
    private static final List<String> requiresDJ = Arrays.asList("play", "playnext", "clear", "background", "loop", "pause", "search", "seek", "shuffle", "skip", "stop");

    private MessageEvent context;
    private Module module;
    private Command command;
    private Member bot;
    private Member commander;

    public CommandExecutor(MessageEvent context) {
        // Is the context null? (Runtime setup for module reflection)
        if(context == null) {
            return;
        }

        this.context = context;
        this.module = context.getModule();
        this.command = context.getCommand();
        this.bot = context.getGuild().getSelfMember();
        this.commander = context.getMember();

        // Is the module enabled and does the command pass the binding checks?
        // Is module named "audio" and if so, does the user fail any of the checks?
        // Is the command on cooldown?
        if(command == null
                || !isEnabled()
                || isBound()
                || !isValidAudio()
                || !command.isCooling(context)
                || !hasPermission()) {
            return;
        }

        // Is the command or module NSFW? If they are, is the channel they're being used in /not/ NSFW?
        if(command.isNSFW() && !context.getChannel().isNSFW()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("invalid_channel", "cmd"))
                    .setDescription(context.i18n("nsfw", "cmd"));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        try {
            log.trace("Invoking {}#onCommand()", command.getClass().getSimpleName());
            command.onCommand(context);
            messageCleanup();
        } catch(Exception e) {
            log.error("Something went wrong when executing the {} command, message: {}", command.getName(), e.getMessage(), e);
            context.getMessage().addReaction("❌").queue();
        }
    }

    /**
     * Loops through each channel and member relevant to the context of the given command and checks permissions.
     * @return boolean
     */
    private boolean hasPermission() {
        if(command.getPermissions() == null) {
            return true;
        }

        for(GuildChannel channel: Stream.of(context.getChannel(), commander.getVoiceState().getChannel()).filter(Objects::nonNull).toList()) {
            for(Member member: Stream.of(bot, commander).filter(Objects::nonNull).toList()) {
                if(member.hasPermission(command.getPermissions()) && !member.hasPermission(channel, command.getPermissions())
                        || !member.hasPermission(command.getPermissions()) && !member.hasPermission(channel, command.getPermissions())) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(context.i18n("missing_permission", "cmd"))
                            .setDescription(context.i18n("missing_permission_member", "cmd").formatted(member.getUser().getAsTag(), command.getPermissions().toString()));
                    MessageDispatcher.reply(context, embed.build());
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks various conditions to see if using certain audio commands are appropriate for the context of the user. Also checks the DJ Mode setting.
     * @return boolean
     */
    private boolean isValidAudio() {
        // If the module isn't the audio module, pass it through.
        if(!module.getName().equals("audio")) {
            return true;
        }

        // Is the member not in a voice channel?
        if(!commander.getVoiceState().inVoiceChannel() && !notInVoiceCommands.contains(command.getName())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("audio_no_channel", "cmd"));
            MessageDispatcher.reply(context, embed.build());
            return false;
        }

        // Does a Lavalink link exist, if so, is the link disconnected and does the command require it to be otherwise?
        if(AudioManager.hasLink(context.getGuild()) && !AudioManager.isLinkConnected(context.getGuild()) && !disconnectedCommands.contains(command.getName())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("audio_no_connection", "cmd"));
            MessageDispatcher.reply(context, embed.build());
            return false;
        }

        // Are there any nodes available?
        if(AudioManager.LAVALINK.getLavalink().getNodes().size() < 1) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("audio_no_lavalink", "cmd"));
            MessageDispatcher.reply(context, embed.build());
            return false;
        }

        // Is the member an administrator?
        if(commander.hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        }

        // Is DJ mode on, if yes does the member lack the DJ role, and if not is the command a DJ mode command?
        if(TextUtilities.toBoolean(GuildFunctions.getGuildSetting("djMode", context.getGuild().getId())) && requiresDJ.contains(command.getName()) && commander.getRoles().stream().noneMatch(role -> role.getName().equals("DJ"))) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("djmode_title", "cmd"))
                    .setDescription(context.i18n("djmode_desc", "cmd"));
            MessageDispatcher.reply(context, embed.build());
            return false;
        }

        return true;
    }

    /**
     * Checks if either the module or command has been disabled.
     * @return boolean
     */
    private boolean isEnabled() {
        // Executor still checks core/developer, in this case simply return false.
        if(constants.contains(module.getName())) {
            return true;
        }

        // Checks if the module is disabled.
        if(!ModuleCommand.DatabaseInterface.isEnabled(context.getGuild().getId(), module.getName())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("module_disabled", "cmd"))
                    .setDescription(context.i18n("module_disabled_desc", "cmd").formatted(module.getName()));
            MessageDispatcher.reply(context, embed.build());
            return false;
        }

        if(!command.isEnabled()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("command_disabled", "cmd"))
                    .setDescription(context.i18n("command_disabled_desc", "cmd").formatted(command.getName()));
            MessageDispatcher.reply(context, embed.build());
            return false;
        }

        return true;
    }

    /**
     * Checks channel bindings to see if commands are allowed to be executed there.
     * @return boolean
     */
    private boolean isBound() {
        if(BindCommand.DatabaseInterface.isBound(context.getGuild().getId(), context.getChannel().getId(), module.getName())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("module_bound", "cmd"))
                    .setDescription(context.i18n("module_bound_desc", "cmd").formatted(command.getName(), BindCommand.DatabaseInterface.getBindsByModule(context.getGuild(), module.getName(), ", ")));
            MessageDispatcher.sendTempMessage(context, embed.build());
            return true;
        }
        return false;
    }

    /**
     * Removes the message that issued the command if the `deleteExecuted` setting is toggled to `on`.
     */
    private void messageCleanup() {
        // Does the server want the command message removed?
        if(TextUtilities.toBoolean(GuildFunctions.getGuildSetting("cleanupcommands", context.getGuild().getId()))) {
            if(bot.hasPermission(Permission.MESSAGE_MANAGE)) { // Can the bot manage messages?
                context.getMessage().delete().queue();
            } else {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("missing_permission", "cmd"))
                        .setDescription(context.i18n("missing_permission_cleanup", "cmd").formatted(context.getPrefix()));
                MessageDispatcher.reply(context, embed.build());
            }
        }
    }

}