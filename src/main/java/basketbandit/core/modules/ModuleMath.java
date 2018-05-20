// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandRoll;
import basketbandit.core.commands.CommandSum;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleMath {

    public ModuleMath(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.ROLL.getEffectiveName())) {
            new CommandRoll(e);
            return;
        }

        if(command.equals(C.SUM.getEffectiveName())) {
            new CommandSum(e);
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleMath.");
    }

}
