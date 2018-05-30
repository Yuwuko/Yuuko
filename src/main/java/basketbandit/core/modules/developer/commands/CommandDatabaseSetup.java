package basketbandit.core.modules.developer.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandDatabaseSetup extends Command {

    public CommandDatabaseSetup() {
        super("dbsetup", "basketbandit.core.modules.developer.ModuleDeveloper", null);
    }

    public CommandDatabaseSetup(MessageReceivedEvent e) {
        super("dbsetup", "basketbandit.core.modules.developer.ModuleDeveloper", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        if(new DatabaseFunctions().setupDatabase()) {
            e.getTextChannel().sendMessage("Database setup successfully.").queue();
        } else {
            e.getTextChannel().sendMessage("Database setup was unsuccessful.").queue();
        }
    }
}
