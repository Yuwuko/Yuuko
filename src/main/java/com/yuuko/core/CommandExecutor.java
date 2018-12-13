package com.yuuko.core;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Sanitise;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static net.dv8tion.jda.core.audio.hooks.ConnectionStatus.NOT_CONNECTED;

public class CommandExecutor {

    public CommandExecutor(MessageReceivedEvent e, String[] command, Module module) {
        if(e != null && command != null) { // Is the command or event null? (This case is used by the M class to initialise a list of modules!)
            if(module.getModuleName().equals("Developer") && !(e.getAuthor().getIdLong() == 215161101460045834L)) { // Is the module named "Developer"? If so, is the author of the message me?
                return;
            } else {
                if(module.checkModuleSettings(e)) { // Is the module enabled?
                    if(module.getModuleName().equals("Audio") && !audioChecks(e, command)) { // Is module named Audio? If so, does the user fail any of the checks?
                        return;
                    }
                    if(!module.isChannelNSFW(e) && module.isModuleNSFW()) { // Is the channel NSFW? If not, is the module NSFW?
                        MessageHandler.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", this command can only be used in NSFW flagged channels.");
                    } else {
                        for(Command cmd : module.getModuleCommands()) {
                            if(cmd.getCommandName().equalsIgnoreCase(command[0])) { // Is the command name the same as the command given? (Ignoring case!)
                                if(cmd.getCommandPermission() != null && !e.getMember().hasPermission(cmd.getCommandPermission()) && !e.getMember().hasPermission(e.getTextChannel(), cmd.getCommandPermission())) { // Is the command permission NULL? If so does the user have the permission?
                                    MessageHandler.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
                                    break;
                                } else {
                                    if(Sanitise.checkParameters(e, command, cmd.getExpectedParameters())) { // Does the command contain the minimum number of parameters?
                                        cmd.executeCommand(e, command);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            messageCleanup(e);
        }
    }

    private void messageCleanup(MessageReceivedEvent e) {
        if(new DatabaseFunctions().getServerSetting("deleteExecuted", e.getGuild().getId()).equals("1")) { // Does the server want the command message removed?
            if(!e.getGuild().getMemberById(420682957007880223L).hasPermission(Permission.MESSAGE_MANAGE)) { // Can the bot manage messages?
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("MESSAGE_MANAGE");
                MessageHandler.sendMessage(e, embed.build());
            } else {
                e.getMessage().delete().queue();
            }
        }
    }

    private boolean audioChecks(MessageReceivedEvent e, String[] command) {
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
            if(new DatabaseFunctions().getServerSetting("djMode", e.getGuild().getId()).equals("1")) {
                if(e.getMember().getRoles().stream().noneMatch(role -> role.getName().equals("DJ"))) {
                    if(!command[0].equals("queue") && !command[0].equals("current") && !command[0].equals("last")) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("While DJ mode is active, only a user with the role of 'DJ' can use that command.");
                        MessageHandler.sendMessage(e, embed.build());
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
