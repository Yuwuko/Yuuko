package com.yuuko.core.modules.utility.commands;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandExclude extends Command {

    public CommandExclude() {
        super("exclude", "com.yuuko.core.modules.utility.ModuleUtility", 1, new String[]{"-exclude [module]", "-exclude [module] [channel]"}, Permission.ADMINISTRATOR);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 2);
        String serverId;
        String channelId;
        String module;

        if(commandParameters.length > 1) {
            serverId = e.getGuild().getId();
            channelId = e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getId();
            module = commandParameters[0].toLowerCase();

            int res = new DatabaseFunctions().toggleExclusion(module, channelId, serverId);

            if(res == 0) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully excluded _" + module + "_ from _" + e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getName() + "_.");
                MessageHandler.sendMessage(e, embed.build());
            } else if(res == 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully included _" + module + "_ into _" + e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getName() + "_.");
                MessageHandler.sendMessage(e, embed.build());
            }

        } else {
            serverId = e.getGuild().getId();
            channelId = e.getTextChannel().getId();
            module = commandParameters[0];
            int res = new DatabaseFunctions().toggleExclusion(module, channelId, serverId);

            if(res == 0) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully excluded _" + module + "_ from _" + e.getTextChannel().getName() + "_.");
                MessageHandler.sendMessage(e, embed.build());
            } else if(res == 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully included _" + module + "_ into _" + e.getTextChannel().getName() + "_.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

}