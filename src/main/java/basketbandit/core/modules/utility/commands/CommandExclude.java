package basketbandit.core.modules.utility.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandExclude extends Command {

    public CommandExclude() {
        super("exclude", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
    }

    public CommandExclude(MessageReceivedEvent e) {
        super("exclude", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
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

        if(new DatabaseFunctions().addExclusion(module,channelId,serverId)) {
            e.getTextChannel().sendMessage("Successfully excluded " + module + " from " + e.getGuild().getTextChannelsByName(commandArray[2], true).get(0).getName() + ".").queue();
        } else {
            e.getTextChannel().sendMessage("Exclude unsuccessful, are you sure this the module isn't already excluded from the channel?").queue();
        }
    }

}