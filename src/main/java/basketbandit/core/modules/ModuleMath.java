package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandRoll;
import basketbandit.core.commands.CommandSum;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleMath extends Module {

    ModuleMath() {
        super("ModuleMath", "modMath");
    }

    public ModuleMath(MessageReceivedEvent e) {
        super("ModuleMath", "modMath");

        if(!checkModuleSettings(e)) {
            return;
        }

        if(!executeCommand(e)) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
        }

    }

    protected boolean executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.ROLL.getEffectiveName())) {
            new CommandRoll(e);
            return true;
        }

        if(command.equals(C.SUM.getEffectiveName())) {
            new CommandSum(e);
            return true;
        }

        return false;
    }
}
