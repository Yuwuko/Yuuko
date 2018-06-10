package com.basketbandit.core.modules.moderation;

import com.basketbandit.core.Utils;
import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.moderation.commands.CommandBan;
import com.basketbandit.core.modules.moderation.commands.CommandKick;
import com.basketbandit.core.modules.moderation.commands.CommandNuke;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleModeration extends Module {

    public ModuleModeration() {
        super("ModuleModeration", "moduleModeration");
    }

    public ModuleModeration(MessageReceivedEvent e, String[] command) {
        super("ModuleModeration", "moduleModeration");

        if(checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.NUKE.getCommandName()) && (e.getMember().hasPermission(C.NUKE.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.NUKE.getCommandPermission()))) {
            new CommandNuke(e, command);
            return;
        }

        if(command[0].equals(C.KICK.getCommandName()) && (e.getMember().hasPermission(C.KICK.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.KICK.getCommandPermission()))){
            new CommandKick(e, command);
            return;
        }

        if(command[0].equals(C.BAN.getCommandName()) && (e.getMember().hasPermission(C.BAN.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.BAN.getCommandPermission()))) {
            new CommandBan(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}
