package com.yuuko.core.controllers;

import com.yuuko.core.modules.audio.commands.CommandStop;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;

public class GenericGuildVoiceController {

    public GenericGuildVoiceController(GenericGuildVoiceEvent e) {
        if(e instanceof GuildVoiceLeaveEvent || e instanceof GuildVoiceMoveEvent) {
            voiceChannelLonelyCheck(e);
        }
    }

    private void voiceChannelLonelyCheck(GenericGuildVoiceEvent e) {
        Member self = e.getGuild().getSelfMember();

        if(self.getVoiceState().inVoiceChannel()) {
            if(self.getVoiceState().getChannel().getMembers().size() == 1) {
                new CommandStop().executeCommand(e);
            }
        }

    }

}
