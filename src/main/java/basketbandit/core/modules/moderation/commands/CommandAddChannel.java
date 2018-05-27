package basketbandit.core.modules.moderation.commands;

import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandAddChannel extends Command {

    public CommandAddChannel() {
        super("addchannel", "basketbandit.core.modules.moderation.ModuleModeration", Permission.MANAGE_CHANNEL);
    }

    public CommandAddChannel(MessageReceivedEvent e) {
        super("addchannel", "basketbandit.core.modules.moderation.ModuleModeration", Permission.MANAGE_CHANNEL);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        String[] command = e.getMessage().getContentRaw().split("\\s+", 2);
        String type = command[1].toLowerCase();

        if(type.equals("text")) {
            e.getGuild().getController().createTextChannel(command[2]).setNSFW(command.length > 3).queue();
        } else if(type.equals("voice")) {
            e.getGuild().getController().createVoiceChannel(command[2]).queue();
        }
    }

}
