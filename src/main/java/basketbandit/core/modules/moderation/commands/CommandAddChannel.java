package basketbandit.core.modules.moderation.commands;

import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandAddChannel extends Command {

    public CommandAddChannel() {
        super("addchannel", "basketbandit.core.modules.moderation.ModuleModeration", Permission.MANAGE_CHANNEL);
    }

    public CommandAddChannel(MessageReceivedEvent e, String[] command) {
        super("addchannel", "basketbandit.core.modules.moderation.ModuleModeration", Permission.MANAGE_CHANNEL);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 2);
        String type = command[1].toLowerCase();

        if(type.equals("text")) {
            e.getGuild().getController().createTextChannel(commandParameters[1]).setNSFW(command.length > 2).queue();
        } else if(type.equals("voice")) {
            e.getGuild().getController().createVoiceChannel(commandParameters[1]).queue();
        }
    }

}
