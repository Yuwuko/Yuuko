package basketbandit.core.modules.moderation.commands;

import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.NoSuchElementException;

public class CommandDeleteChannel extends Command {

    public CommandDeleteChannel() {
        super("delchannel", "basketbandit.core.modules.moderation.ModuleModeration", Permission.MANAGE_CHANNEL);
    }

    public CommandDeleteChannel(MessageReceivedEvent e) {
        super("delchannel", "basketbandit.core.modules.moderation.ModuleModeration", Permission.MANAGE_CHANNEL);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException;
     */
    protected void executeCommand(MessageReceivedEvent e) throws NoSuchElementException {
        String[] command = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String type = command[1];
        Long idLong = Long.parseLong(command[2]);
        TextChannel textChannel = e.getGuild().getTextChannelById(idLong);
        VoiceChannel voiceChannel = e.getGuild().getVoiceChannelById(idLong);

        if(type.equals("text")) {
            if(textChannel == null) throw new NoSuchElementException();
            e.getGuild().getTextChannelById(command[2]).delete().queue();
        } else if(type.equals("voice") && command[2].length() == 18 && Long.parseLong(command[2]) > 0) {
            if(voiceChannel == null) throw new NoSuchElementException();
            e.getGuild().getVoiceChannelById(command[2]).delete().queue();
        }
    }

}
