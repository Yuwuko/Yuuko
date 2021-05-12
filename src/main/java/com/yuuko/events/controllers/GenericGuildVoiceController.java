package com.yuuko.events.controllers;

import com.yuuko.modules.audio.commands.StopCommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

public class GenericGuildVoiceController {

    public GenericGuildVoiceController(GenericGuildVoiceEvent event) {
        if(event instanceof GuildVoiceLeaveEvent || event instanceof GuildVoiceMoveEvent) {
            voiceChannelAbandonedCheck(event);
        }
    }

    private void voiceChannelAbandonedCheck(GenericGuildVoiceEvent event) {
        GuildVoiceState voiceState = event.getGuild().getSelfMember().getVoiceState();
        if(voiceState != null && voiceState.inVoiceChannel() && voiceState.getChannel().getMembers().size() == 1) {
            new StopCommand().onCommand(event.getGuild());
        }
    }
}
