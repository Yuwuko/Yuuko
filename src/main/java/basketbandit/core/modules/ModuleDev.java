// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandDatabaseSetup;
import basketbandit.core.commands.CommandSetStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleDev {

    public ModuleDev(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+",2);
        String command = commandArray[0];

        // Just in case.
        if(e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        if(command.equals(C.SET_STATUS.getEffectiveName())) {
            new CommandSetStatus(e);
            return;
        }

        if(command.equals(C.DATABASE_SETUP.getEffectiveName())) {
            new CommandDatabaseSetup(e);
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleDev.");
    }

}
