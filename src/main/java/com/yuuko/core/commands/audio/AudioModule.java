package com.yuuko.core.commands.audio;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.commands.*;
import com.yuuko.core.events.extensions.MessageEvent;

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
            new SeekCommand(),
            new LyricsCommand()
    };

    public AudioModule(MessageEvent e) {
        super("Audio", false, commands);
        new CommandExecutor(e, this);
    }

}
