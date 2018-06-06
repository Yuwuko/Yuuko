package basketbandit.core.modules.utility.commands;

import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandUnexclude extends Command {

    public CommandUnexclude() {
        super("unexclude", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
    }

    public CommandUnexclude(MessageReceivedEvent e, String[] command) {
        super("unexclude", "basketbandit.core.modules.utility.ModuleUtility", Permission.ADMINISTRATOR);
        executeCommand(e, command);
    }

    /**
     * A bit cheeky but to keep the commands working correctly and to reduce duplicate code,
     * I just pass this command over to the unbind command. Perhaps I should implement aliases?
     * @param e MessageReceivedEvent
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        new CommandUnbind(e, command);
    }

}