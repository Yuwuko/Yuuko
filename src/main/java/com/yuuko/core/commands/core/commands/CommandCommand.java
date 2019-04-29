package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.database.CommandFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandCommand extends Command {

    public CommandCommand() {
        super("command", CoreModule.class, 0, new String[]{"-command", "-command reset", "-command <command>", "-command <command> <channel>", "-command <command> reset"}, false, new Permission[]{Permission.MANAGE_SERVER});
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(e.hasParameters()) {
            String[] input = e.getCommand()[1].toLowerCase().split("\\s+", 2);
            TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);

            List<String> commandWords = Arrays.asList("all", "*", "global", "globally", "everywhere", "anywhere");

            boolean valid = false;

            // Reset all commands.
            if(input[0].equals("reset")) {
                CommandFunctions.resetCommandSettings(e.getGuild(), null);
                EmbedBuilder embed = new EmbedBuilder().setTitle("Commands Reset").setDescription("All commands have been reset, enabling them everywhere.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            if(!commandWords.contains(input[0])) {
                // Check if the command even exists.
                for(Command commandObj : Configuration.COMMANDS.values()) {
                    if(commandObj.getName().equals(input[0])) {
                        if(Utilities.getModuleName(commandObj.getModule()).equals("Core")) {
                            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Command").setDescription("Sorry, you cannot disable commands from the `Core` module.");
                            MessageHandler.sendMessage(e, embed.build());
                            return;
                        }
                        valid = true;
                        break;
                    }
                }
            } else {
                valid = true;
            }

            if(!valid) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Command").setDescription("`" + input[0] + "` is not a command and therefore cannot be disabled.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            // Reset single command instead of all commands.
            if(input.length > 1 && input[1].equals("reset")) {
                CommandFunctions.resetCommandSettings(e.getGuild(), input[0]);
                EmbedBuilder embed = new EmbedBuilder().setTitle("Command Reset").setDescription("The " + input[0] + " command has been successfully reset, enabling it everywhere.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            String commandInput = (commandWords.contains(input[0])) ? "All commands were " : "`" + input[0] + "` was ";
            String channelInput = (channel == null) ? "on the server!" : "in " + channel.getName() + "!";

            if(CommandFunctions.toggleCommand(e.getGuild().getId(), (channel == null) ? "*" : channel.getId(), (commandWords.contains(input[0])) ? "*" : input[0])) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("Command(s) Enabled").setDescription(commandInput + " enabled " + channelInput);
                MessageHandler.sendMessage(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("Command(s) Disabled").setDescription(commandInput + " disabled " + channelInput);
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            ArrayList<String> settings = CommandFunctions.getCommandSettings(e.getGuild().getId());

            EmbedBuilder commandModules = new EmbedBuilder()
                    .setTitle("Below is a list of the disabled commands!")
                    .setDescription("Each command can be toggled on or off globally by using the `" + Utilities.getServerPrefix(e.getGuild()) + "command <command>` command, or to an individual channel by using `" + Utilities.getServerPrefix(e.getGuild()) + "command <command> #channel`")
                    .addField("Disabled Commands (" + settings.size() + ")", TextUtilities.formatArray(settings.toString()), true)
                    .setTimestamp(Instant.now())
                    .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, commandModules.build());
        }
    }
}
