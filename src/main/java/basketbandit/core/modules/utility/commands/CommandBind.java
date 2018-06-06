package basketbandit.core.modules.utility.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandBind extends Command {

    public CommandBind() {
        super("bind", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
    }

    public CommandBind(MessageReceivedEvent e, String[] command) {
        super("bind", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 2);

        String serverId = e.getGuild().getId();
        String channelId = e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getId();
        String module = command[1];

        if(!new DatabaseFunctions().addBinding(module,channelId,serverId)) {
            e.getTextChannel().sendMessage("Successfully bound " + module + " to " + e.getGuild().getTextChannelsByName(commandParameters[1], true).get(0).getName() + ".").queue();
        } else {
            e.getTextChannel().sendMessage("Bind unsuccessful, are you sure this the module isn't already bound to the channel?").queue();
        }
    }

}
