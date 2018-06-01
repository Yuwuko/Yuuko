package basketbandit.core.modules.math;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.math.commands.CommandRoll;
import basketbandit.core.modules.math.commands.CommandSum;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleMath extends Module {

    public ModuleMath() {
        super("ModuleMath", "moduleMath");
    }

    public ModuleMath(MessageReceivedEvent e, String prefix) {
        super("ModuleMath", "moduleMath");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, prefix);
    }

    protected void executeCommand(MessageReceivedEvent e, String prefix) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0].substring(prefix.length());

        if(command.equals(C.ROLL.getCommandName())) {
            new CommandRoll(e);
            return;
        }

        if(command.equals(C.SUM.getCommandName())) {
            new CommandSum(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
