package basketbandit.core.modules.custom.commands;

import basketbandit.core.Configuration;
import basketbandit.core.Database;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.NoSuchElementException;

public class CommandAddCustom extends Command {

    public CommandAddCustom() {
        super(Configuration.PREFIX + "addcc", "basketbandit.core.modules.custom.ModuleCustom", null);
    }

    public CommandAddCustom(MessageReceivedEvent e) {
        super(Configuration.PREFIX + "addcc", "basketbandit.core.modules.custom.ModuleCustom", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException ;
     */
    protected void executeCommand(MessageReceivedEvent e) throws NoSuchElementException {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("",3);
        String serverLong = e.getGuild().getIdLong()+"";
        Database database = new Database();

        if(commandArray[2].length() > 2000) {
            e.getTextChannel().sendMessage("Command too long, maximum length of 2000 characters.").queue();
            return;
        }

        if(database.addCustomCommand(commandArray[1], commandArray[2], serverLong, e.getAuthor().getIdLong()+"")) {
            e.getTextChannel().sendMessage(commandArray[1] + " added successfully!").queue();
        } else {
            e.getTextChannel().sendMessage("Failed to add command...").queue();
        }
    }

}
