package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandLineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleTransport extends Module {

    ModuleTransport() {
        super("ModuleTransport", "modTransport");
    }

    public ModuleTransport(MessageReceivedEvent e) {
        super("ModuleTransport", "modTransport");

        if(!checkModuleSettings(e)) {
            return;
        }

        if(!executeCommand(e)) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleTransport.");
    }

    protected boolean executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.LINE_STATUS.getEffectiveName())) {
            new CommandLineStatus(e);
            return true;
        }

        return false;
    }
}
