package basketbandit.core.modules.developer;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.developer.commands.CommandDatabaseSetup;
import basketbandit.core.modules.developer.commands.CommandSetStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleDeveloper extends Module {

    public ModuleDeveloper() {
        super("ModuleDeveloper", "modDev");
    }

    public ModuleDeveloper(MessageReceivedEvent e) {
        super("ModuleDeveloper", "modDev");

        // Just in case.
        if(e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        executeCommand(e);
    }

    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+",2);
        String command = commandArray[0];

        if(command.contains(C.SET_STATUS.getCommandName())) {
            new CommandSetStatus(e);
            return;
        }

        if(command.contains(C.DATABASE_SETUP.getCommandName())) {
            new CommandDatabaseSetup(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
