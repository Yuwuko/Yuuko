package basketbandit.core.modules;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class ModuleLogging extends Module {

    ModuleLogging() {
        super("ModuleLogging", "modLogging");
    }

    public ModuleLogging(MessageReceivedEvent e) {
        super("ModuleLogging", "modLogging");

        // We don't check inside the class if the module is active because the module
        // because it would be checked after every message and the Module super class
        // displays a message if it is off.

        if(!executeCommand(e)) {
            e.getTextChannel().sendMessage("This server has logging enabled but has no 'command-log' text channel, please add that channel or disable the module by typing the '" + Configuration.PREFIX + "module logging' command to stop this message.").queue();
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleLogging.");
    }

    protected boolean executeCommand(MessageReceivedEvent e) {
        try {
            List<TextChannel> log = e.getGuild().getTextChannelsByName("command-log", true);
            if(log.size() > 0) {
                log.get(0).sendMessage("```" + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " used command " + e.getMessage().getContentDisplay() + " in " + e.getMessage().getChannel().getName() + ".```").queue();
            }
            return true;
        } catch(Exception err) {
            return false;
        }
    }
}
