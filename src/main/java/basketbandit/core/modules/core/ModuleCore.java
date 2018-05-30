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
        executeCommand(e);
    }

    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 2);
        String command = commandArray[0];

        if(command.contains(C.SETUP.getCommandName()) && e.getMember().hasPermission(C.SETUP.getCommandPermission())) {
            new CommandSetup(e);
            return;
        }

        if(command.contains(C.MODULE.getCommandName()) && e.getMember().hasPermission(C.MODULE.getCommandPermission())) {
            new CommandModule(e);
            return;
        }

        if(command.contains(C.MODULES.getCommandName())) {
            new CommandModules(e);
            return;
        }

        if(command.contains(C.HELP.getCommandName())) {
            new CommandHelp(e);
            return;
        }

        if(command.contains(C.ABOUT.getCommandName())) {
            new CommandAbout(e);
            return;
        }

        if(command.contains(C.SET_PREFIX.getCommandName()) && e.getMember().hasPermission(C.SET_PREFIX.getCommandPermission())) {
            new CommandSetPrefix(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }

}
