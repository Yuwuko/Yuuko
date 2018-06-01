package basketbandit.core.modules.developer;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.developer.commands.CommandSetStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleDeveloper extends Module {

    public ModuleDeveloper() {
        super("ModuleDeveloper", null);
    }

    public ModuleDeveloper(MessageReceivedEvent e, String prefix) {
        super("ModuleDeveloper", null);

        // Just in case.
        if(e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        executeCommand(e, prefix);
    }

    protected void executeCommand(MessageReceivedEvent e, String prefix) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+",2);
        String command = commandArray[0].substring(prefix.length());

        if(command.equals(C.SET_STATUS.getCommandName())) {
            new CommandSetStatus(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
