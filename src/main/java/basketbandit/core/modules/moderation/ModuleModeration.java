package basketbandit.core.modules.moderation;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.moderation.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleModeration extends Module {

    public ModuleModeration() {
        super("ModuleModeration", "modModeration");
    }

    public ModuleModeration(MessageReceivedEvent e) {
        super("ModuleModeration", "modModeration");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e);
    }

    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.contains(C.NUKE.getCommandName()) && e.getMember().hasPermission(C.NUKE.getCommandPermission())) {
            new CommandNuke(e);
            return;
        }

        if(command.contains(C.KICK.getCommandName()) && e.getMember().hasPermission(C.KICK.getCommandPermission())){
            new CommandKick(e);
            return;
        }

        if(command.contains(C.BAN.getCommandName()) && e.getMember().hasPermission(C.BAN.getCommandPermission())) {
            new CommandBan(e);
            return;
        }

        if(command.contains(C.CREATE_CHANNEL.getCommandName()) && e.getMember().hasPermission(C.CREATE_CHANNEL.getCommandPermission())) {
            new CommandAddChannel(e);
            return;
        }

        if(command.contains(C.DELETE_CHANNEL.getCommandName()) && e.getMember().hasPermission(C.DELETE_CHANNEL.getCommandPermission())) {
            new CommandDeleteChannel(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
