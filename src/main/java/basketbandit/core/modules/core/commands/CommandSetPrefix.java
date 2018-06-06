package basketbandit.core.modules.core.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetPrefix extends Command {

    public CommandSetPrefix() {
        super("setprefix", "basketbandit.core.modules.core.ModuleCore", Permission.ADMINISTRATOR);
    }

    public CommandSetPrefix(MessageReceivedEvent e, String[] command) {
        super("setprefix", "basketbandit.core.modules.core.ModuleCore", Permission.ADMINISTRATOR);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String value = command[1].toLowerCase();
        String serverLong = e.getGuild().getId();

        if(!new DatabaseFunctions().setServerPrefix(value, serverLong)) {
            e.getTextChannel().sendMessage("Server prefix set to: " + value).queue();
        }
    }

}
