package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandAddCustom;
import basketbandit.core.commands.CommandCustom;
import basketbandit.core.commands.CommandDeleteCustom;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleCustom extends Module {

    ModuleCustom() {
        super("ModuleCustom", "modCustom");
    }

    public ModuleCustom(MessageReceivedEvent e) {
        super("ModuleCustom", "modCustom");

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

        if(command.equals(C.ADD_CUSTOM.getEffectiveName())) {
            new CommandAddCustom(e);
            return true;
        }

        if(command.equals(C.DELETE_CUSTOM.getEffectiveName())) {
            new CommandDeleteCustom(e);
            return true;
        }

        if(command.startsWith(C.CUSTOM.getEffectiveName())) {
            new CommandCustom(e);
            return true;
        }

        return false;
    }

}