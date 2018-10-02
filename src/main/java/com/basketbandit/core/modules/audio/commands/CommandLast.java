package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.ModuleAudio;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.Utils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandLast extends Command {

	public CommandLast() {
		super("last", "com.basketbandit.core.modules.audio.ModuleAudio", new String[]{"-last"}, null);
	}

	public CommandLast(MessageReceivedEvent e, String[] command) {
		executeCommand(e, command);
	}

	@Override
	protected void executeCommand(MessageReceivedEvent e, String[] command) {
		GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());
		AudioTrack track = manager.scheduler.getLastTrack();
		String[] uri = track.getInfo().uri.split("=");
		String imageUrl = (uri.length > 1) ? "https://img.youtube.com/vi/" + uri[1] + "/1.jpg" : "https://i.imgur.com/bCNQlm6.jpg";

		if(manager.scheduler.getLastTrack() != null) {
			EmbedBuilder queuedTrack = new EmbedBuilder()
					.setColor(Color.DARK_GRAY)
					.setAuthor("Last track:")
					.setTitle(track.getInfo().title, track.getInfo().uri)
					.setThumbnail(imageUrl)
					.addField("Duration", ModuleAudio.getTimestamp(track.getDuration()), true)
					.addField("Channel", track.getInfo().author, true)
					.setFooter(Configuration.VERSION + ", requested by " + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

			Utils.sendMessage(e, queuedTrack.build());
		} else {
			Utils.sendMessage(e, "Sorry, there is no 'last track' available...");
		}

	}
}
