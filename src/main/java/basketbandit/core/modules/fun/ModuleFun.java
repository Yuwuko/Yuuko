package basketbandit.core.modules.fun;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.fun.commands.CommandInsult;
import basketbandit.core.modules.fun.commands.CommandOverreact;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleFun extends Module {

    public ModuleFun() {
        super("ModuleFun", "modFun");
    }

    public ModuleFun(MessageReceivedEvent e) {
        super("ModuleFun", "modFun");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e);
    }

    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.contains(C.OVERREACT.getCommandName())) {
            new CommandOverreact(e);
            return;
        }

        if(command.contains(C.INSULT.getCommandName())) {
            new CommandInsult(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
