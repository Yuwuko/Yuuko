package com.basketbandit.core.modules.developer;

import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.developer.commands.CommandSetStatus;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleDeveloper extends Module {

    public ModuleDeveloper() {
        super("ModuleDeveloper", null);
    }

    public ModuleDeveloper(MessageReceivedEvent e, String[] command) {
        super("ModuleDeveloper", null);

        if(e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.SET_STATUS.getCommandName())) {
            new CommandSetStatus(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}
