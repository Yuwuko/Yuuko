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

        if(command.equals(C.SET_STATUS.getEffectiveName())) {
            new CommandSetStatus(e);
            return;
        }

        if(command.equals(C.DATABASE_SETUP.getEffectiveName())) {
            new CommandDatabaseSetup(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
