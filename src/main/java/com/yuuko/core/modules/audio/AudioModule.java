package com.yuuko.core.modules.audio;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.audio.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AudioModule extends Module {

    public AudioModule(MessageReceivedEvent e, String[] command) {
        super("Audio", "moduleAudio", false, new Command[]{
                new PlayCommand(),
                new PauseCommand(),
                new StopCommand(),
                new SearchCommand(),
                new SkipCommand(),
                new ClearCommand(),
                new QueueCommand(),
                new BackgroundCommand(),
                new CurrentCommand(),
                new LastCommand(),
                new ShuffleCommand(),
                new RepeatCommand()
        });

        new CommandExecutor(e, command, this);
    }

}
