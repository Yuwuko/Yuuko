package basketbandit.core.commands;

import basketbandit.core.Configuration;
import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandQueue extends Command {

    CommandQueue() {
        super("queue", "basketbandit.core.modules.ModuleMusic", null);
    }

    public CommandQueue(MessageReceivedEvent e) {
        super("queue", "basketbandit.core.modules.ModuleMusic", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     *
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());

        synchronized(manager.scheduler.queue) {
            StringBuilder queue = new StringBuilder();
            int i = 1;

            for(AudioTrack track : manager.scheduler.queue) {
                queue.append(i).append(": ").append(track.getInfo().title).append(", (").append(ModuleMusic.getTimestamp(track.getInfo().length)).append(") \n");
                i++;
                if (i > 10) {
                    break;
                }
            }
            i--;

            if(i > 0) {
                EmbedBuilder nextTracks = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setAuthor("Hey " + e.getMember().getEffectiveName() + ",", null, e.getAuthor().getAvatarUrl())
                        .setTitle("Here are the next " + i + " tracks in the queue:")
                        .setDescription(queue.toString())
                        .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                e.getTextChannel().sendMessage(nextTracks.build()).queue();
            } else {
                e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the queue is empty!").queue();
            }
        }
        return true;

    }

}
