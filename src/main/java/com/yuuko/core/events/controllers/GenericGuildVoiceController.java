package com.yuuko.core.events.controllers;

import com.yuuko.core.commands.audio.commands.StopCommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

public class GenericGuildVoiceController {

    public GenericGuildVoiceController(GenericGuildVoiceEvent e) {
        if(e instanceof GuildVoiceLeaveEvent || e instanceof GuildVoiceMoveEvent) {
            voiceChannelLonelyCheck(e);
        }
    }

    private void voiceChannelLonelyCheck(GenericGuildVoiceEvent e) {
        GuildVoiceState voice = e.getGuild().getSelfMember().getVoiceState();

        if(voice.inVoiceChannel() && voice.getChannel().getMembers().size() == 1) {
            new StopCommand().onCommand(e.getGuild());
        }
    }

}
