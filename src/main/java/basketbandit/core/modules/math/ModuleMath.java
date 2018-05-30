package basketbandit.core.modules.math;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.math.commands.CommandRoll;
import basketbandit.core.modules.math.commands.CommandSum;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleMath extends Module {

    public ModuleMath() {
        super("ModuleMath", "modMath");
    }

    public ModuleMath(MessageReceivedEvent e) {
        super("ModuleMath", "modMath");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e);
    }

    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.contains(C.ROLL.getCommandName())) {
            new CommandRoll(e);
            return;
        }

        if(command.contains(C.SUM.getCommandName())) {
            new CommandSum(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
