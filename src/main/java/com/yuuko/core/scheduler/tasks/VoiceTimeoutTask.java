package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.commands.audio.commands.StopCommand;
import com.yuuko.core.scheduler.Task;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;

public class VoiceTimeoutTask implements Task {
    private final Guild guild;
    private final GuildVoiceState voiceState;

    public VoiceTimeoutTask(Guild guild) {
        this.guild = guild;
        this.voiceState = guild.getSelfMember().getVoiceState();
    }

    @Override
    public void run() {
        if(voiceState != null && voiceState.inVoiceChannel()) {
            new StopCommand().onCommand(guild);
        }
    }

}
