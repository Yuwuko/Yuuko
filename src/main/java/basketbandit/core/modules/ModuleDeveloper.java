package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandDatabaseSetup;
import basketbandit.core.commands.CommandSetStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleDeveloper extends Module {

    ModuleDeveloper() {
        super("ModuleDeveloper", "modDev");
    }

    public ModuleDeveloper(MessageReceivedEvent e) {
        super("ModuleDeveloper", "modDev");

        // Just in case.
        if(e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        if(!checkModuleSettings(e)) {
            return;
        }

        if(!executeCommand(e)) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleDeveloper.");
    }

    protected boolean executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+",2);
        String command = commandArray[0];

        if(command.equals(C.SET_STATUS.getEffectiveName())) {
            new CommandSetStatus(e);
            return true;
        }

        if(command.equals(C.DATABASE_SETUP.getEffectiveName())) {
            new CommandDatabaseSetup(e);
            return true;
        }

        return false;
    }
}
