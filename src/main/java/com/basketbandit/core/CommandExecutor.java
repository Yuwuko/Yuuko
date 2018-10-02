package com.basketbandit.core;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.utils.Sanitise;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandExecutor {

    public CommandExecutor(MessageReceivedEvent e, String[] command, Module module) {
        Member member = e.getMember();

        for(Command cmd : module.getModuleCommands()) {
            if(cmd.getCommandName().equalsIgnoreCase(command[0])) {
                if(cmd.getCommandPermission() != null) {
                    if(!member.hasPermission(cmd.getCommandPermission()) || !member.hasPermission(e.getTextChannel(), cmd.getCommandPermission())) {
                        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
                        return;
                    }
                }
                if(Sanitise.checkParameters(e, command, cmd.getExpectedParameters())) {
                    cmd.executeCommand(e, command);
                }
                return;
            }
        }

    }

}
