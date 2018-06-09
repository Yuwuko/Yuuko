package basketbandit.core.modules.utility.commands;

import basketbandit.core.Utils;
import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandExclude extends Command {

    public CommandExclude() {
        super("exclude", "basketbandit.core.modules.utility.ModuleUtility", new String[]{"-exclude [module]", "-exclude [module] [channel]"}, Permission.ADMINISTRATOR);
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
            module = commandParameters[0];

            if(!new DatabaseFunctions().addExclusion(module, channelId, serverId)) {
                Utils.sendMessage(e, "Successfully excluded " + module + " from " + e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getName() + ".");
            } else {
                Utils.sendMessage(e, "Exclusion unsuccessful, are you sure this the module isn't already excluded from the channel?");
            }
        } else {
            serverId = e.getGuild().getId();
            channelId = e.getTextChannel().getId();
            module = commandParameters[0];

            if(!new DatabaseFunctions().addExclusion(module, channelId, serverId)) {
                Utils.sendMessage(e, "Successfully excluded " + module + " from " + e.getTextChannel().getName() + ".");
            } else {
                Utils.sendMessage(e, "Exclusion unsuccessful, are you sure this the module isn't already excluded to the channel?");
            }
        }
    }

}