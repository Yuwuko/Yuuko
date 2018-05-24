package basketbandit.core.commands;

import basketbandit.core.Configuration;
import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandLastTrack extends Command {

    CommandLastTrack() {
        super("lasttrack", "basketbandit.core.modules.ModuleMusic", null);
    }

    public CommandLastTrack(MessageReceivedEvent e) {
        super("lasttrack", "basketbandit.core.modules.ModuleMusic", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());
        AudioTrackInfo track = manager.scheduler.getLastTrack().getInfo();

        if(manager.scheduler.getLastTrack() != null) {
            EmbedBuilder queuedTrack = new EmbedBuilder()
                    .setColor(Color.WHITE)
                    .setTitle(track.title, track.uri)
                    .setDescription("Channel: " + track.author + ", Duration: " + ModuleMusic.getTimestamp(track.length))
                    .setFooter("Version: " + Configuration.VERSION + ", Requested by " + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

            e.getTextChannel().sendMessage(queuedTrack.build()).queue();
            return true;
        } else {
            e.getTextChannel().sendMessage("There is no last track...").queue();
            return false;
        }

    }
}
