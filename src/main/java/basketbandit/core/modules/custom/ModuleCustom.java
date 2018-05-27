package basketbandit.core.modules.custom;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.custom.commands.CommandAddCustom;
import basketbandit.core.modules.custom.commands.CommandCustom;
import basketbandit.core.modules.custom.commands.CommandDeleteCustom;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleCustom extends Module {

    public ModuleCustom() {
        super("ModuleCustom", "modCustom");
    }

    public ModuleCustom(MessageReceivedEvent e) {
        super("ModuleCustom", "modCustom");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e);
    }

    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.ADD_CUSTOM.getEffectiveName())) {
            new CommandAddCustom(e);
            return;
        }

        if(command.equals(C.DELETE_CUSTOM.getEffectiveName())) {
            new CommandDeleteCustom(e);
            return;
        }

        if(command.startsWith(C.CUSTOM.getEffectiveName())) {
            new CommandCustom(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }

}