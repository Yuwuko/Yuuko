package com.yuuko.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.commands.audio.handlers.AudioManager;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.utilities.TextUtilities;
import com.yuuko.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class LastCommand extends Command {

	public LastCommand() {
		super("last", Yuuko.MODULES.get("audio"), 0, -1L, Arrays.asList("-last"), false, null);
	}

	@Override
	public void onCommand(MessageEvent e) throws Exception {
		AudioTrack track = AudioManager.getGuildAudioManager(e.getGuild()).getPlayer().getPlayingTrack();
		if(track != null) {
			EmbedBuilder queuedTrack = new EmbedBuilder()
					.setAuthor("Last track")
					.setTitle(track.getInfo().title, track.getInfo().uri)
					.setThumbnail(Utilities.getAudioTrackImage(track))
					.addField("Duration", TextUtilities.getTimestamp(track.getDuration()), true)
					.addField("Channel", track.getInfo().author, true)
					.setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getAvatarUrl());
			MessageDispatcher.reply(e, queuedTrack.build());
			return;
		}

		EmbedBuilder embed = new EmbedBuilder().setTitle("There isn't a previous track to return.");
		MessageDispatcher.reply(e, embed.build());
	}

}
