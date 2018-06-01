package basketbandit.core.modules.moderation;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.moderation.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleModeration extends Module {

    public ModuleModeration() {
        super("ModuleModeration", "moduleModeration");
    }

    public ModuleModeration(MessageReceivedEvent e, String prefix) {
        super("ModuleModeration", "moduleModeration");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, prefix);
    }

    protected void executeCommand(MessageReceivedEvent e, String prefix) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0].substring(prefix.length());

        if(command.equals(C.NUKE.getCommandName()) && e.getMember().hasPermission(C.NUKE.getCommandPermission())) {
            new CommandNuke(e);
            return;
        }

        if(command.equals(C.KICK.getCommandName()) && e.getMember().hasPermission(C.KICK.getCommandPermission())){
            new CommandKick(e);
            return;
        }

        if(command.equals(C.BAN.getCommandName()) && e.getMember().hasPermission(C.BAN.getCommandPermission())) {
            new CommandBan(e);
            return;
        }

        if(command.equals(C.CREATE_CHANNEL.getCommandName()) && e.getMember().hasPermission(C.CREATE_CHANNEL.getCommandPermission())) {
            new CommandAddChannel(e);
            return;
        }

        if(command.equals(C.DELETE_CHANNEL.getCommandName()) && e.getMember().hasPermission(C.DELETE_CHANNEL.getCommandPermission())) {
            new CommandDeleteChannel(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
