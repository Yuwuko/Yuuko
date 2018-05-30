package basketbandit.core.modules.logging;

import basketbandit.core.Configuration;
import basketbandit.core.modules.Module;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class ModuleLogging extends Module {

    public ModuleLogging() {
        super("ModuleLogging", "modLogging");
    }

    public ModuleLogging(MessageReceivedEvent e) {
        super("ModuleLogging", "modLogging");

        // We don't check inside the class if the module is active because the module
        // because it would be checked after every message and the Module super class
        // displays a message if it is off.
        executeCommand(e);
    }

    protected void executeCommand(MessageReceivedEvent e) {
        try {
            List<TextChannel> log = e.getGuild().getTextChannelsByName("command-log", true);
            if(log.size() > 0) {
                log.get(0).sendMessage("```" + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " used command " + e.getMessage().getContentDisplay() + " in " + e.getMessage().getChannel().getName() + ".```").queue();
            }
        } catch(Exception ex) {
            e.getTextChannel().sendMessage("This server has logging enabled but has no 'command-log' text channel, please add that channel or disable the module by typing the '" + Configuration.GLOBAL_PREFIX + "module logging' command to stop this message.").queue();
        }
    }
}
