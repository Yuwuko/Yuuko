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

    public CommandBan(MessageReceivedEvent e, String[] command) {
        super("ban", "basketbandit.core.modules.moderation.ModuleModeration", Permission.BAN_MEMBERS);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException;
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) throws NoSuchElementException {
        String[] commandParameters = command[1].split("\\s+", 3);
        Long value = Long.parseLong(command[1]);
        int time = Integer.parseInt(commandParameters[1]);
        Member member = e.getGuild().getMemberById(value);

        if(member == null) throw new NoSuchElementException();

        if(commandParameters.length < 3) {
            e.getGuild().getController().ban(commandParameters[0], time).queue();
        } else {
            e.getGuild().getController().ban(commandParameters[0], time, commandParameters[2]).queue();
        }
    }

}
