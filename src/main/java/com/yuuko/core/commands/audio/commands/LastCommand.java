package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class LastCommand extends Command {

	public LastCommand() {
		super("last", Config.MODULES.get("audio"), 0, -1L, Arrays.asList("-last"), false, null);
	}

	@Override
	public void onCommand(MessageEvent e) {
		try {
			AudioTrack track = AudioManagerController.getGuildAudioManager(e.getGuild()).getPlayer().getPlayingTrack();

			if(track != null) {
				EmbedBuilder queuedTrack = new EmbedBuilder()
						.setAuthor("Last track")
						.setTitle(track.getInfo().title, track.getInfo().uri)
						.setThumbnail(Utilities.getAudioTrackImage(track))
						.addField("Duration", TextUtilities.getTimestamp(track.getDuration()), true)
						.addField("Channel", track.getInfo().author, true)
						.setFooter(Config.STANDARD_STRINGS.get(0), Config.BOT.getAvatarUrl());
				MessageHandler.sendMessage(e, queuedTrack.build());
			} else {
				EmbedBuilder embed = new EmbedBuilder().setTitle("There isn't a previous track to return.");
				MessageHandler.reply(e, embed.build());
			}

		} catch(Exception ex) {
			log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
		}
	}

}
