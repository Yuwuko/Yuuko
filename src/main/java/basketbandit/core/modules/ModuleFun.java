// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandInsult;
import basketbandit.core.commands.CommandOverreact;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleFun {

    public ModuleFun(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.OVERREACT.getEffectiveName())) {
            new CommandOverreact(e);
            return;
        }

        if(command.equals(C.INSULT.getEffectiveName())) {
            new CommandInsult(e);
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleFun.");
    }
}
