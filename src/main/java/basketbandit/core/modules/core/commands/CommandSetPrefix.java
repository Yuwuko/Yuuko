package basketbandit.core.modules.core.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetPrefix extends Command {

    public CommandSetPrefix() {
        super("setprefix", "basketbandit.core.modules.core.ModuleCore", Permission.ADMINISTRATOR);
    }

    public CommandSetPrefix(MessageReceivedEvent e) {
        super("setprefix", "basketbandit.core.modules.core.ModuleCore", Permission.ADMINISTRATOR);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 2);
        String value = commandArray[1].toLowerCase();
        String serverLong = e.getGuild().getId();

        if(!new DatabaseFunctions().setServerPrefix(value, serverLong)) {
            e.getTextChannel().sendMessage("Server prefix set to: " + value).queue();
        }
    }

}
