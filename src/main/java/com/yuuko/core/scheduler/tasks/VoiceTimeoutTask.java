package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.commands.audio.commands.StopCommand;
import com.yuuko.core.scheduler.Task;
import net.dv8tion.jda.api.entities.Guild;

public class VoiceTimeoutTask implements Task {
    private final Guild guild;

    public VoiceTimeoutTask(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void run() {
        if(guild.getSelfMember().getVoiceState().inVoiceChannel()) {
            new StopCommand().onCommand(guild);
        }
    }

}
