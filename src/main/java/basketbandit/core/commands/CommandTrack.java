package basketbandit.core.commands;

import basketbandit.core.Configuration;
import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandTrack extends Command {

    CommandTrack() {
        super("track", "music", null);
    }

    public CommandTrack(MessageReceivedEvent e) {
        super("track", "music", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());

        if(manager.player.getPlayingTrack() != null) {
            EmbedBuilder queuedTrack = new EmbedBuilder()
                    .setColor(Color.WHITE)
                    .setTitle(manager.player.getPlayingTrack().getInfo().title, manager.player.getPlayingTrack().getInfo().uri)
                    .setDescription("Channel: " + manager.player.getPlayingTrack().getInfo().author + ", Duration: " + ModuleMusic.getTimestamp(manager.player.getPlayingTrack().getInfo().length))
                    .setFooter("Version: " + Configuration.VERSION + ", Requested by " + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

            e.getTextChannel().sendMessage(queuedTrack.build()).queue();
            return true;
        } else {
            e.getTextChannel().sendMessage("Nothing is currently playing...").queue();
            return false;
        }

    }

}
