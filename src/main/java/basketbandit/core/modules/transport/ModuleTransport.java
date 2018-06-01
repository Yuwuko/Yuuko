package basketbandit.core.modules.transport;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.transport.commands.CommandLineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleTransport extends Module {

    public ModuleTransport() {
        super("ModuleTransport", "moduleTransport");
    }

    public ModuleTransport(MessageReceivedEvent e, String prefix) {
        super("ModuleTransport", "moduleTransport");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, prefix);
    }

    protected void executeCommand(MessageReceivedEvent e, String prefix) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0].substring(prefix.length());

        if(command.equals(C.LINE_STATUS.getCommandName())) {
            new CommandLineStatus(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
