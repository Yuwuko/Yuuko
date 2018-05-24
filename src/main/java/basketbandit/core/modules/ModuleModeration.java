package basketbandit.core.modules;

import basketbandit.core.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleModeration extends Module {

    ModuleModeration() {
        super("ModuleModeration", "modModeration");
    }

    public ModuleModeration(MessageReceivedEvent e) {
        super("ModuleModeration", "modModeration");

        if(!checkModuleSettings(e)) {
            return;
        }

        if(!executeCommand(e)) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleModeration.");
    }

    protected boolean executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.NUKE.getEffectiveName()) && e.getMember().hasPermission(C.NUKE.getCommandPermission())) {
            new CommandNuke(e);
            return true;
        }

        if(command.equals(C.KICK.getEffectiveName()) && e.getMember().hasPermission(C.KICK.getCommandPermission())){
            new CommandKick(e);
            return true;
        }

        if(command.equals(C.BAN.getEffectiveName()) && e.getMember().hasPermission(C.BAN.getCommandPermission())) {
            new CommandBan(e);
            return true;
        }

        if(command.equals(C.CREATE_CHANNEL.getEffectiveName()) && e.getMember().hasPermission(C.CREATE_CHANNEL.getCommandPermission())) {
            new CommandAddChannel(e);
            return true;
        }

        if(command.equals(C.DELETE_CHANNEL.getEffectiveName()) && e.getMember().hasPermission(C.DELETE_CHANNEL.getCommandPermission())) {
            new CommandDeleteChannel(e);
            return true;
        }

        return false;
    }
}
