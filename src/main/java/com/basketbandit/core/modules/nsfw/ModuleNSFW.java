package com.basketbandit.core.modules.nsfw;

import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.nsfw.commands.CommandEfukt;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleNSFW extends Module {

    public ModuleNSFW() {
        super("ModuleNSFW", "moduleNSFW");
    }

    public ModuleNSFW(MessageReceivedEvent e, String[] command) {
        super("ModuleNSFW", "moduleNSFW");

        if(!checkNSFWFlag(e)) {
            Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", this command can only be used in NSFW flagged channels.");
            return;
        }

        if(checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.EFUKT.getCommandName())) {
            new CommandEfukt(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}
