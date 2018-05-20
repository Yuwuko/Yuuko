package basketbandit.core.commands;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.NoSuchElementException;

public class CommandOverreact extends Command {

    CommandOverreact() {
        super("overreact", "fun", null);
    }

    public CommandOverreact(MessageReceivedEvent e) {
        super("overreact", "fun", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException ;
     */
    protected boolean executeCommand(MessageReceivedEvent e) throws NoSuchElementException {
        List<Message> messageList = e.getTextChannel().getHistory().retrievePast(10).complete();

        int i = 0;
        for(Emote emote : e.getGuild().getEmotes()) {
            if(i < 20) {
                messageList.get(0).addReaction(emote).queue();
                i++;
            }
        }
        return true;
    }

    // TODO: Redo overreact command. (Choose random emotes)
}
