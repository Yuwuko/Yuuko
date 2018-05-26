package basketbandit.core.modules.custom.commands;

import basketbandit.core.Configuration;
import basketbandit.core.Database;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.NoSuchElementException;

public class CommandCustom extends Command {

    public CommandCustom() {
        super(Configuration.PREFIX, "basketbandit.core.modules.custom.ModuleCustom", null);
    }

    public CommandCustom(MessageReceivedEvent e) {
        super(Configuration.PREFIX, "basketbandit.core.modules.custom.ModuleCustom", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException ;
     */
    protected boolean executeCommand(MessageReceivedEvent e) throws NoSuchElementException {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 3);
        String serverLong = e.getGuild().getIdLong()+"";
        String command = commandArray[0].replace(Configuration.PREFIX + Configuration.PREFIX, "");

        Database database = new Database();
        String commandContent = database.getCustomCommand(command, serverLong);

        if(commandContent == null) throw new NoSuchElementException();

        e.getTextChannel().sendMessage(commandContent).queue();
        return true;

    }

}
