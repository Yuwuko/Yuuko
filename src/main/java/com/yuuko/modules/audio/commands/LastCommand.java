package com.yuuko.modules.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
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
	public void onCommand(MessageEvent context) throws Exception {
		AudioTrack track = AudioManager.getGuildAudioManager(context.getGuild()).getPlayer().getPlayingTrack();

		if(track == null) {
			EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "no_track"));
			MessageDispatcher.reply(context, embed.build());
			return;
		}

		EmbedBuilder queuedTrack = new EmbedBuilder()
				.setAuthor(context.i18n( "author"))
				.setTitle(track.getInfo().title, track.getInfo().uri)
				.setThumbnail(Utilities.getAudioTrackImage(track))
				.addField(context.i18n( "duration"), TextUtilities.getTimestamp(track.getDuration()), true)
				.addField(context.i18n( "channel"), track.getInfo().author, true)
				.setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getAvatarUrl());
		MessageDispatcher.reply(context, queuedTrack.build());
	}

}
