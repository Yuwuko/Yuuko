package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandLineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleTransport {

    public ModuleTransport(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.LINE_STATUS.getEffectiveName())) {
            new CommandLineStatus(e);
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleTransport.");
    }

}

