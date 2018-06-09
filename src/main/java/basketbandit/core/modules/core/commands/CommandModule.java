package basketbandit.core.modules.core.commands;

import basketbandit.core.Utils;
import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandModule extends Command {

    public CommandModule() {
        super("module", "basketbandit.core.modules.core.ModuleCore", new String[]{"-module [module]"}, Permission.MANAGE_PERMISSIONS);
    }

    public CommandModule(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }


    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String moduleName = command[1].toLowerCase();
        String serverLong = e.getGuild().getId();

        // Check if the module even exists.
        boolean hit = false;
        for(Module module: Utils.moduleList) {
            if(module.getModuleName().substring(6).toLowerCase().equals(command[1])) {
                hit = true;
                break;
            }
        }

        if(!hit) {
            Utils.sendMessage(e, moduleName + " is not a valid module.");
            return;
        }

        if(new DatabaseFunctions().toggleModule("module" + moduleName, serverLong)) {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setAuthor(moduleName + " was enabled on this server!");
            Utils.sendMessage(e, embed.build());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor(moduleName + " was disabled on this server!");
            Utils.sendMessage(e, embed.build());
        }

    }

}
