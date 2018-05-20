// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleModeration {

    /**
     * Module constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    public ModuleModeration(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.NUKE.getEffectiveName()) && e.getMember().hasPermission(C.NUKE.getCommandPermission())) {
            new CommandNuke(e);
            return;
        }

        if(command.equals(C.KICK.getEffectiveName()) && e.getMember().hasPermission(C.KICK.getCommandPermission())){
            new CommandKick(e);
            return;
        }

        if(command.equals(C.BAN.getEffectiveName()) && e.getMember().hasPermission(C.BAN.getCommandPermission())) {
            new CommandBan(e);
            return;
        }

        if(command.equals(C.CREATE_CHANNEL.getEffectiveName()) && e.getMember().hasPermission(C.CREATE_CHANNEL.getCommandPermission())) {
            new CommandAddChannel(e);
            return;
        }

        if(command.equals(C.DELETE_CHANNEL.getEffectiveName()) && e.getMember().hasPermission(C.DELETE_CHANNEL.getCommandPermission())) {
            new CommandDeleteChannel(e);
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleModeration.");
    }

}
