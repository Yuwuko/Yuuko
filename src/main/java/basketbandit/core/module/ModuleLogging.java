// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

class ModuleLogging {

    private MessageReceivedEvent e;

    ModuleLogging(MessageReceivedEvent e) {
        this.e = e;
        commandLogCommand();
    }

    /**
     * Attempts to log the most recent command into the command-log channel.
     */
    private void commandLogCommand() {
        try {
            List<TextChannel> log = e.getGuild().getTextChannelsByName("command-log", true);
            if(log.size() > 0) {
                log.get(0).sendMessage("```" + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " used command " + e.getMessage().getContentDisplay() + " in " + e.getMessage().getChannel().getName() + ".```").queue();
            }
        } catch(Exception err) {
            e.getTextChannel().sendMessage("This server has logging enabled but has no 'command-log' text channel, please add that channel or disable the module by typing the '" + Configuration.PREFIX + "module logging' command to stop this message.").queue();
        }
    }
}
