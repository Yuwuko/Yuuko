// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleCore {

    /**
     * Module constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    public ModuleCore(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.SETUP.getEffectiveName()) && e.getMember().hasPermission(C.SETUP.getCommandPermission())) {
            new CommandSetup(e);
            return;
        }

        if(command.equals(C.MODULE.getEffectiveName()) && e.getMember().hasPermission(C.MODULE.getCommandPermission())) {
            new CommandModule(e);
            return;
        }

        if(command.equals(C.MODULES.getEffectiveName())) {
            new CommandModules(e);
            return;
        }

        if(command.equals(C.HELP.getEffectiveName())) {
            new CommandHelp(e);
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleCore.");
    }

}
