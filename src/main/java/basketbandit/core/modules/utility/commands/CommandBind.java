package basketbandit.core.modules.utility.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandBind extends Command {

    public CommandBind() {
        super("bind", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
    }

    public CommandBind(MessageReceivedEvent e) {
        super("bind", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 3);
        String serverId = e.getGuild().getId();
        String channelId = e.getGuild().getTextChannelsByName(commandArray[2], true).get(0).getId();
        String module = commandArray[1];

        if(!new DatabaseFunctions().addBinding(module,channelId,serverId)) {
            e.getTextChannel().sendMessage("Successfully bound " + module + " to " + e.getGuild().getTextChannelsByName(commandArray[2], true).get(0).getName() + ".").queue();
        } else {
            e.getTextChannel().sendMessage("Bind unsuccessful, are you sure this the module isn't already bound to the channel?").queue();
        }
    }

}
