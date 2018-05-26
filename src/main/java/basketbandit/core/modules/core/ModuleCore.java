package basketbandit.core.modules.core;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.core.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleCore extends Module {

    public ModuleCore() {
        super("ModuleCore", null);
    }

    public ModuleCore(MessageReceivedEvent e) {
        super("ModuleCore", null);

        // Core module doesn't have database checks because it cannot be toggled
        // since it is a core part of the bot and has useful modules that aren't
        // to do with moderation and are more about the bot itself.

        if(!executeCommand(e)) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
        }

    }

    protected boolean executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.SETUP.getEffectiveName()) && e.getMember().hasPermission(C.SETUP.getCommandPermission())) {
            new CommandSetup(e);
            return true;
        }

        if(command.equals(C.MODULE.getEffectiveName()) && e.getMember().hasPermission(C.MODULE.getCommandPermission())) {
            new CommandModule(e);
            return true;
        }

        if(command.equals(C.MODULES.getEffectiveName())) {
            new CommandModules(e);
            return true;
        }

        if(command.equals(C.HELP.getEffectiveName())) {
            new CommandHelp(e);
            return true;
        }

        if(command.equals(C.ABOUT.getEffectiveName())) {
            new CommandAbout(e);
            return true;
        }

        return false;
    }

}
