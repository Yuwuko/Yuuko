package basketbandit.core.modules.utility.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandUnbind extends Command {

    public CommandUnbind() {
        super("unbind", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
    }

    public CommandUnbind(MessageReceivedEvent e) {
        super("unbind", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 3);
        String serverId = e.getGuild().getId();
        String channelId = e.getGuild().getTextChannelsByName(commandArray[2], true).get(0).getId();
        String module = commandArray[1];

        if(!new DatabaseFunctions().removeBindingExclusion(module,channelId,serverId)) {
            e.getTextChannel().sendMessage("Successfully removed binding/exclusion of " + module + " from " + e.getGuild().getTextChannelsByName(commandArray[2], true).get(0).getName() + ".").queue();
        } else {
            e.getTextChannel().sendMessage("Removal unsuccessful, are you sure this the module is bound/excluded to/from the channel?").queue();
        }
    }

}
