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

    public CommandDeleteChannel(MessageReceivedEvent e, String[] command) {
        super("delchannel", "basketbandit.core.modules.moderation.ModuleModeration", Permission.MANAGE_CHANNEL);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException;
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) throws NoSuchElementException {
        String[] commandParameters = command[1].split("\\s+", 2);
        String type = command[1];
        Long idLong = Long.parseLong(commandParameters[1]);
        TextChannel textChannel = e.getGuild().getTextChannelById(idLong);
        VoiceChannel voiceChannel = e.getGuild().getVoiceChannelById(idLong);

        if(type.equals("text")) {
            if(textChannel == null) throw new NoSuchElementException();
            e.getGuild().getTextChannelById(commandParameters[1]).delete().queue();
        } else if(type.equals("voice") && commandParameters[1].length() == 18 && Long.parseLong(commandParameters[1]) > 0) {
            if(voiceChannel == null) throw new NoSuchElementException();
            e.getGuild().getVoiceChannelById(commandParameters[1]).delete().queue();
        }
    }

}
