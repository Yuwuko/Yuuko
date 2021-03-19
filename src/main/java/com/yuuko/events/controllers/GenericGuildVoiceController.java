package com.yuuko.events.controllers;

import com.yuuko.commands.audio.commands.StopCommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

public class GenericGuildVoiceController {

    public GenericGuildVoiceController(GenericGuildVoiceEvent e) {
        if(e instanceof GuildVoiceLeaveEvent || e instanceof GuildVoiceMoveEvent) {
            voiceChannelAbandonedCheck(e);
        }
    }

    private void voiceChannelAbandonedCheck(GenericGuildVoiceEvent e) {
        GuildVoiceState voiceState = e.getGuild().getSelfMember().getVoiceState();

        if(voiceState != null && voiceState.inVoiceChannel() && voiceState.getChannel().getMembers().size() == 1) {
            new StopCommand().onCommand(e.getGuild());
        }
    }

}
