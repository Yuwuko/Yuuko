package com.yuuko.modules.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.utilities.TextUtilities;
import com.yuuko.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class LastCommand extends Command {

	public LastCommand() {
		super("last", 0, -1L, Arrays.asList("-last"), false, null);
	}

	@Override
	public void onCommand(MessageEvent e) throws Exception {
		AudioTrack track = AudioManager.getGuildAudioManager(e.getGuild()).getPlayer().getPlayingTrack();

		if(track == null) {
			EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getError(e, "no_track"));
			MessageDispatcher.reply(e, embed.build());
			return;
		}

		EmbedBuilder queuedTrack = new EmbedBuilder()
				.setAuthor(I18n.getText(e, "author"))
				.setTitle(track.getInfo().title, track.getInfo().uri)
				.setThumbnail(Utilities.getAudioTrackImage(track))
				.addField(I18n.getText(e, "duration"), TextUtilities.getTimestamp(track.getDuration()), true)
				.addField(I18n.getText(e, "channel"), track.getInfo().author, true)
				.setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getAvatarUrl());
		MessageDispatcher.reply(e, queuedTrack.build());
	}

}
