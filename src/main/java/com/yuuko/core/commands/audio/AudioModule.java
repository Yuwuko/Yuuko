package com.yuuko.core.commands.audio;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.commands.*;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class AudioModule extends Module {
    private static final List<Command> commands = Arrays.asList(
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
            new SeekCommand(),
            new LyricsCommand()
    );

    public AudioModule(MessageEvent e) {
        super("audio", false, commands);
        if(e != null) {
            new CommandExecutor(e.setModule(this));
        }
    }

}
