package basketbandit.core.modules.moderation.commands;

import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.NoSuchElementException;

public class CommandBan extends Command {

    public CommandBan() {
        super("ban", "basketbandit.core.modules.moderation.ModuleModeration", Permission.BAN_MEMBERS);
    }

    public CommandBan(MessageReceivedEvent e) {
        super("ban", "basketbandit.core.modules.moderation.ModuleModeration", Permission.BAN_MEMBERS);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException;
     */
    protected boolean executeCommand(MessageReceivedEvent e) throws NoSuchElementException {
        String[] command = e.getMessage().getContentRaw().split("\\s+", 3);
        long value = Long.parseLong(command[1]);
        int time = Integer.parseInt(command[2]);
        Member member = e.getGuild().getMemberById(value);

        if(member == null) throw new NoSuchElementException();

        if(command.length < 4) {
            e.getGuild().getController().ban(command[1], time).queue();
        } else {
            e.getGuild().getController().ban(command[1], time, command[3]).queue();
        }
        return true;

    }

}
