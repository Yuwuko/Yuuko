// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandRuneScapeStats;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleRuneScape {

    public ModuleRuneScape(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.RUNESCAPE_STATS.getEffectiveName())) {
            new CommandRuneScapeStats(e);
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleRuneScape.");
    }

}
