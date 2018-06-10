package com.basketbandit.core.modules.utility.commands;

import com.basketbandit.core.Utils;
import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandUnbind extends Command {

    public CommandUnbind() {
        super("unbind", "com.basketbandit.core.modules.utility.ModuleUtility", new String[]{"-unbind [module]", "-unbind [module] [channel]"}, Permission.ADMINISTRATOR);
    }

    public CommandUnbind(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 2);
        String serverId;
        String channelId ;
        String module ;

        if(commandParameters.length > 1) {
            serverId = e.getGuild().getId();
            channelId = e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getId();
            module = commandParameters[0].toLowerCase();

            if(new DatabaseFunctions().removeBindingExclusion(module, channelId, serverId)) {
                Utils.sendMessage(e, "Successfully removed binding of " + module + " from " + e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getName() + ".");
            } else {
                Utils.sendMessage(e, "Removal unsuccessful, are you sure this module is bound to the channel?");
            }
        } else {
            serverId = e.getGuild().getId();
            channelId = e.getTextChannel().getId();
            module = commandParameters[0];

            if(new DatabaseFunctions().removeBindingExclusion(module, channelId, serverId)) {
                Utils.sendMessage(e, "Successfully removed binding of " + module + " from " + e.getTextChannel().getName() + ".");
            } else {
                Utils.sendMessage(e, "Removal unsuccessful, are you sure this module is bound to the channel?");
            }
        }
    }

}
