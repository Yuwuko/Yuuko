package basketbandit.core.modules.core;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.core.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleCore extends Module {

    public ModuleCore() {
        super("ModuleCore", null);
    }

    public ModuleCore(MessageReceivedEvent e, String prefix) {
        super("ModuleCore", null);

        // Core module doesn't have database checks because it cannot be toggled
        // since it is a core part of the bot and has useful modules that aren't
        // to do with moderation and are more about the bot itself.
        executeCommand(e, prefix);
    }

    protected void executeCommand(MessageReceivedEvent e, String prefix) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 2);
        String command = commandArray[0].substring(prefix.length());

        if(command.equals(C.SETUP.getCommandName()) && e.getMember().hasPermission(C.SETUP.getCommandPermission())) {
            new CommandSetup(e);
            return;
        }

        if(command.equals(C.MODULE.getCommandName()) && e.getMember().hasPermission(C.MODULE.getCommandPermission())) {
            new CommandModule(e);
            return;
        }

        if(command.equals(C.MODULES.getCommandName())) {
            new CommandModules(e);
            return;
        }

        if(command.equals(C.HELP.getCommandName())) {
            new CommandHelp(e);
            return;
        }

        if(command.equals(C.ABOUT.getCommandName())) {
            new CommandAbout(e);
            return;
        }

        if(command.equals(C.SET_PREFIX.getCommandName()) && e.getMember().hasPermission(C.SET_PREFIX.getCommandPermission())) {
            new CommandSetPrefix(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.").queue();
    }

}
