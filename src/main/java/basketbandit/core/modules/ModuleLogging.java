// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class ModuleLogging {

    private MessageReceivedEvent e;

    public ModuleLogging(MessageReceivedEvent e) {
        this.e = e;
        logCommand();
    }

    /**
     * Attempts to log the most recent command into the command-log channel.
     */
    private void logCommand() {
        try {
            List<TextChannel> log = e.getGuild().getTextChannelsByName("command-log", true);
            if(log.size() > 0) {
                log.get(0).sendMessage("```" + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " used command " + e.getMessage().getContentDisplay() + " in " + e.getMessage().getChannel().getName() + ".```").queue();
            }
        } catch(Exception err) {
            e.getTextChannel().sendMessage("This server has logging enabled but has no 'command-log' text channel, please add that channel or disable the modules by typing the '" + Configuration.PREFIX + "modules logging' command to stop this message.").queue();
        }
    }
}
