package basketbandit.core.modules.moderation;

import basketbandit.core.Utils;
import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.moderation.commands.CommandBan;
import basketbandit.core.modules.moderation.commands.CommandKick;
import basketbandit.core.modules.moderation.commands.CommandNuke;
import basketbandit.core.modules.utility.commands.CommandAddChannel;
import basketbandit.core.modules.utility.commands.CommandDeleteChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleModeration extends Module {

    public ModuleModeration() {
        super("ModuleModeration", "moduleModeration");
    }

    public ModuleModeration(MessageReceivedEvent e, String[] command) {
        super("ModuleModeration", "moduleModeration");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.NUKE.getCommandName()) && e.getMember().hasPermission(C.NUKE.getCommandPermission())) {
            new CommandNuke(e, command);
            return;
        }

        if(command[0].equals(C.KICK.getCommandName()) && e.getMember().hasPermission(C.KICK.getCommandPermission())){
            new CommandKick(e, command);
            return;
        }

        if(command[0].equals(C.BAN.getCommandName()) && e.getMember().hasPermission(C.BAN.getCommandPermission())) {
            new CommandBan(e, command);
            return;
        }

        if(command[0].equals(C.CREATE_CHANNEL.getCommandName()) && e.getMember().hasPermission(C.CREATE_CHANNEL.getCommandPermission())) {
            new CommandAddChannel(e, command);
            return;
        }

        if(command[0].equals(C.DELETE_CHANNEL.getCommandName()) && e.getMember().hasPermission(C.DELETE_CHANNEL.getCommandPermission())) {
            new CommandDeleteChannel(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}
