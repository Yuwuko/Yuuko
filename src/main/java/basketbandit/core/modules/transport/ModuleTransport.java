package basketbandit.core.modules.transport;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.transport.commands.CommandLineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleTransport extends Module {

    public ModuleTransport() {
        super("ModuleTransport", "moduleTransport");
    }

    public ModuleTransport(MessageReceivedEvent e, String[] command) {
        super("ModuleTransport", "moduleTransport");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.LINE_STATUS.getCommandName())) {
            new CommandLineStatus(e, command);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.").queue();
    }
}
