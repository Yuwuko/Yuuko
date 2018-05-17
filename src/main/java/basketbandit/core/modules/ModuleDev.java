// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.Configuration;
import basketbandit.core.Database;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleDev {

    public ModuleDev(MessageReceivedEvent e) {
        Message message = e.getMessage();
        String[] command = message.getContentRaw().split("\\s+",2);

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "dbsetup")) {
            commandSetup();
        }
    }

    /**
     * Sets up the database.
     */
    private void commandSetup() {
        Database database = new Database();
        database.setupDatabase();
    }
}
