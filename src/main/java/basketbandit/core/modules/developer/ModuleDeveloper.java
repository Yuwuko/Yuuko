package basketbandit.core.modules.developer;

import basketbandit.core.Utils;
import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.developer.commands.CommandSetStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleDeveloper extends Module {

    public ModuleDeveloper() {
        super("ModuleDeveloper", null);
    }

    public ModuleDeveloper(MessageReceivedEvent e, String[] command) {
        super("ModuleDeveloper", null);

        // Just in case.
        if(e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        executeCommand(e, command);
    }

    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.SET_STATUS.getCommandName())) {
            new CommandSetStatus(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}
