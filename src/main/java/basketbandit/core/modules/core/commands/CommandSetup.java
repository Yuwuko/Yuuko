package basketbandit.core.modules.core.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetup extends Command {

    public CommandSetup() {
        super("setup", "basketbandit.core.modules.core.ModuleCore", Permission.ADMINISTRATOR);
    }

    public CommandSetup(MessageReceivedEvent e) {
        super("setup", "basketbandit.core.modules.core.ModuleCore", Permission.ADMINISTRATOR);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        String serverLong = e.getGuild().getId();

        if(!new DatabaseFunctions().addNewServer(serverLong)) {
            e.getTextChannel().sendMessage("Server setup successful. (You cannot do this again!)").queue();
        } else {
            e.getTextChannel().sendMessage("Server setup was unsuccessful. (Are you sure the setup command has not been run before?)").queue();
        }
    }

}
