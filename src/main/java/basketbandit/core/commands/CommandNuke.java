package basketbandit.core.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.List;

public class CommandNuke extends Command {

    CommandNuke() {
        super("nuke", "basketbandit.core.modules.ModuleModeration", Permission.MESSAGE_MANAGE);
    }

    public CommandNuke(MessageReceivedEvent e) {
        super("nuke", "basketbandit.core.modules.ModuleModeration", Permission.MESSAGE_MANAGE);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws IllegalArgumentException;
     */
    protected boolean executeCommand(MessageReceivedEvent e) throws IllegalArgumentException {
            String[] command = e.getMessage().getContentRaw().split("\\s+", 2);

            int value = Integer.parseInt(command[1]);
            if(value < 1 || value > 100) throw new IllegalArgumentException();

            List<Message> nukeList = e.getTextChannel().getHistory().retrievePast(value).complete();

            if(value < 2) {
                e.getTextChannel().deleteMessageById(nukeList.get(0).getId()).complete();
            } else {
                // If a message in the nuke list is older than 2 weeks it can't be mass deleted, so recursion will need to take place.
                for(Message message: nukeList) {
                    if(message.getCreationTime().isBefore(OffsetDateTime.now().minusWeeks(2))) {
                        message.delete().complete();
                    }
                }
                e.getGuild().getTextChannelById(e.getTextChannel().getId()).deleteMessages(nukeList).complete();
            }

            return true;
    }
}
