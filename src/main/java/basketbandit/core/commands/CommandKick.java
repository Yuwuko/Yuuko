package basketbandit.core.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.NoSuchElementException;

public class CommandKick extends Command {

    CommandKick() {
        super("kick", "basketbandit.core.modules.moderation", Permission.KICK_MEMBERS);
    }

    public CommandKick(MessageReceivedEvent e) {
        super("kick", "basketbandit.core.modules.moderation", Permission.KICK_MEMBERS);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException;
     */
    protected boolean executeCommand(MessageReceivedEvent e) throws NoSuchElementException {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 3);
        long value = Long.parseLong(commandArray[1]);
        Member member = e.getGuild().getMemberById(value);

        if(member == null) throw new NoSuchElementException();

        if(commandArray.length < 3) {
            e.getGuild().getController().kick(commandArray[1]).queue();
        } else {
            e.getGuild().getController().kick(commandArray[1], commandArray[2]).queue();
        }
        return true;

    }

}
