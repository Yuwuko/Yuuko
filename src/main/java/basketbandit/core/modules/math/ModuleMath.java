package basketbandit.core.modules.math;

import basketbandit.core.Utils;
import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.math.commands.CommandRoll;
import basketbandit.core.modules.math.commands.CommandSum;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleMath extends Module {

    public ModuleMath() {
        super("ModuleMath", "moduleMath");
    }

    public ModuleMath(MessageReceivedEvent e, String[] command) {
        super("ModuleMath", "moduleMath");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    protected void executeCommand(MessageReceivedEvent e, String[] command) {


        if(command[0].equals(C.ROLL.getCommandName())) {
            new CommandRoll(e, command);
            return;
        }

        if(command[0].equals(C.SUM.getCommandName())) {
            new CommandSum(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}
