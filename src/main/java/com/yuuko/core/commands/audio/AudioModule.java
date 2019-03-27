package com.yuuko.core.commands.audio;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AudioModule extends Module {
    private static final Command[] commands = new Command[]{
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
            new LoopCommand(),
            new SeekCommand()
    };

    public AudioModule(MessageReceivedEvent e, String[] command) {
        super("Audio", "moduleAudio", false, commands);
        new CommandExecutor(e,this, command);
    }

}
