// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

class ModuleDev {

    ModuleDev(MessageReceivedEvent e) {
        Message message = e.getMessage();
        MessageChannel channel = e.getChannel();
        User user = e.getAuthor();
        Member member = e.getMember();

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
