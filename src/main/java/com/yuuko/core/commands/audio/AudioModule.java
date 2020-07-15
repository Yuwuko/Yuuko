package com.yuuko.core.commands.audio;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.commands.*;

import java.util.Arrays;
import java.util.List;

public class AudioModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new PlayCommand(),
            new PlayNextCommand(),
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

    public AudioModule() {
        super("audio", false, commands);
    }

}
