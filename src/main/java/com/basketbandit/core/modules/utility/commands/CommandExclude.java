package com.basketbandit.core.modules.utility.commands;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandExclude extends Command {

    public CommandExclude() {
        super("exclude", "com.basketbandit.core.modules.utility.ModuleUtility", new String[]{"-exclude [module]", "-exclude [module] [channel]"}, Permission.ADMINISTRATOR);
    }

    public CommandExclude(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
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
                Utils.sendMessage(e, "Successfully excluded " + module + " from " + e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getName() + ".");
            } else if(res == 1) {
                Utils.sendMessage(e, "Successfully included " + module + " to " + e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getName() + ".");
            }
        } else {
            serverId = e.getGuild().getId();
            channelId = e.getTextChannel().getId();
            module = commandParameters[0];

            int res = new DatabaseFunctions().toggleExclusion(module, channelId, serverId);

            if(res == 0) {
                Utils.sendMessage(e, "Successfully excluded " + module + " from " + e.getTextChannel().getName() + ".");
            } else if(res == 1) {
                Utils.sendMessage(e, "Successfully included " + module + " to " + e.getTextChannel().getName() + ".");
            }
        }
    }

}