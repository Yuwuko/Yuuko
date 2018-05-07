// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

class ModuleCustom {

    private MessageReceivedEvent e;
    private String serverLong = "";

    ModuleCustom(MessageReceivedEvent e) {
        this.e = e;
        serverLong = e.getGuild().getIdLong() + "";

        String[] command = e.getMessage().getContentRaw().split("\\s+", 3);

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "newcc")) {
            addCustomCommand(command[1], command[2]);
        } else if(command[0].toLowerCase().startsWith(Configuration.PREFIX + Configuration.PREFIX)) {
            commandCustom(command);
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "delcc")) {
            removeCustomCommand(command[1]);
        }


    }

    /**
     * Runs a custom command.
     * @param command the name of the command.
     */
    private void commandCustom(String[] command) {
        try {
            Database database = new Database();
            String commandReturn = database.getCustomCommand(command[0].replace("??", ""), serverLong);

            e.getTextChannel().sendMessage(commandReturn).queue();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new custom command.
     * @param commandName the name of the command.
     * @param commandContents the contents of the command.
     */
    private void addCustomCommand(String commandName, String commandContents) {
        Database database = new Database();

        if(commandContents.length() > 2000) {
            e.getTextChannel().sendMessage("Command too long, maximum length of 2000 characters.").queue();
            return;
        }

        if(database.addCustomCommand(commandName, commandContents, serverLong, e.getAuthor().getIdLong()+"")) {
            e.getTextChannel().sendMessage(commandName + " added successfully!").queue();
        } else {
            e.getTextChannel().sendMessage("Failed to add command...").queue();
        }
    }

    /**
     * Removes a custom command.
     * @param commandName command name.
     */
    private void removeCustomCommand(String commandName) {
        Database database = new Database();
        if(database.removeCustomCommand(commandName, serverLong)) {
            e.getTextChannel().sendMessage(commandName + " sucessfully removed!").queue();
        } else {
            e.getTextChannel().sendMessage("Failed to remove command...").queue();
        }
    }
}