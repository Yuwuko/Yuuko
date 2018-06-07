package basketbandit.core.modules.moderation.commands;

import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.NoSuchElementException;

public class CommandKick extends Command {

    public CommandKick() {
        super("kick", "basketbandit.core.modules.moderation.ModuleModeration", Permission.KICK_MEMBERS);
    }

    public CommandKick(MessageReceivedEvent e, String[] command) {
        super("kick", "basketbandit.core.modules.moderation.ModuleModeration", Permission.KICK_MEMBERS);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException;
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 3);
        List<Member> mentioned = e.getMessage().getMentionedMembers();
        Member target;

        if(mentioned.size() > 0) {
            target = mentioned.get(0);
        } else {
            target = e.getGuild().getMemberById(Long.parseLong(command[1]));
        }

        if(target == null) {
            e.getTextChannel().sendMessage("Sorry, that user could not be found.").queue();
            return;
        }

        if(commandParameters.length < 3) {
            e.getGuild().getController().kick(target).queue();
        } else {
            e.getGuild().getController().kick(target, commandParameters[1]).queue();
        }
    }

}
