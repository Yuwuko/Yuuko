package com.yuuko.core;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Module;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Sanitiser;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static net.dv8tion.jda.core.audio.hooks.ConnectionStatus.NOT_CONNECTED;

public class CommandExecutor {

    public CommandExecutor(MessageReceivedEvent e, String[] cmd, Module module) {
        if(e != null && cmd != null) { // Is the command or event null? (This case is used by the M class to initialise a list of modules!)
            if(module.getModuleName().equals("Developer") && !(e.getAuthor().getIdLong() == 215161101460045834L)) { // Is the module named "Developer"? If so, is the author of the message me?
                return;
            } else {
                if(module.checkModuleSettings(e)) { // Is the module enabled?
                    if(module.getModuleName().equals("Audio") && !audioChecks(e, cmd)) { // Is module named Audio? If so, does the user fail any of the checks?
                        return;
                    }
                    if(!module.isChannelNSFW(e) && module.isModuleNSFW()) { // Is the channel NSFW? If not, is the module NSFW?
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Channel").setDescription("That command can only be used in NSFW marked channels.");
                        MessageHandler.sendMessage(e, embed.build());
                    } else {
                        module.getModuleCommandsList().stream().filter(command -> command.getCommandName().equalsIgnoreCase(cmd[0])).findFirst().ifPresent(command -> {
                            if(command.getCommandPermissions() != null && !e.getGuild().getMemberById(Configuration.BOT_ID).hasPermission(command.getCommandPermissions())) { // Is the command permission NULL? If so, does the bot have the permission?
                                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the '**" + Utils.getCommandPermissions(command.getCommandPermissions()) + "**' permissions to use that command.");
                                MessageHandler.sendMessage(e, embed.build());
                            } else {
                                if(command.getCommandPermissions() != null && !e.getMember().hasPermission(command.getCommandPermissions()) && !e.getMember().hasPermission(e.getTextChannel(), command.getCommandPermissions())) { // Is the command permission NULL? If so, does the user have the permission?
                                    EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("You require the '**" + Utils.getCommandPermissions(command.getCommandPermissions()) + "**' permissions to use that command.");
                                    MessageHandler.sendMessage(e, embed.build());
                                } else {
                                    if(Sanitiser.checkParameters(e, cmd, command.getExpectedParameters())) { // Does the command contain the minimum number of parameters?
                                        command.executeCommand(e, cmd);
                                    }
                                }
                            }
                        });
                    }
                }
            }
            messageCleanup(e);
        }
    }

    private void messageCleanup(MessageReceivedEvent e) {
        if(new DatabaseFunctions().getServerSetting("deleteExecuted", e.getGuild().getId()).equals("1")) { // Does the server want the command message removed?
            if(!e.getGuild().getMemberById(420682957007880223L).hasPermission(Permission.MESSAGE_MANAGE)) { // Can the bot manage messages?
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I am missing the '**MESSAGE_MANAGE**' permission required to execute the 'deleteExecuted' setting. If this setting is active by mistake, use **'@Yuuko settings deleteExecuted false'** to turn it off.");
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
