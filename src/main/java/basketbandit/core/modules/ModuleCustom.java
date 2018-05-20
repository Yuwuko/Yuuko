// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandAddCustom;
import basketbandit.core.commands.CommandCustom;
import basketbandit.core.commands.CommandDeleteCustom;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleCustom {

    public ModuleCustom(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.ADD_CUSTOM.getEffectiveName())) {
            new CommandAddCustom(e);
            return;
        }

        if(command.equals(C.DELETE_CUSTOM.getEffectiveName())) {
            new CommandDeleteCustom(e);
            return;
        }

        if(command.startsWith(C.CUSTOM.getEffectiveName())) {
            new CommandCustom(e);
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleCustom.");
    }

}