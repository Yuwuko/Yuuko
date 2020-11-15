package com.yuuko.core.commands.audio;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.commands.*;

import java.util.Map;

import static java.util.Map.entry;

public class AudioModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("play", PlayCommand.class),
            entry("playnext", PlayNextCommand.class),
            entry("pause", PauseCommand.class),
            entry("stop", StopCommand.class),
            entry("search", SearchCommand.class),
            entry("skip", SkipCommand.class),
            entry("clear", ClearCommand.class),
            entry("queue", QueueCommand.class),
            entry("background", BackgroundCommand.class),
            entry("current", CurrentCommand.class),
            entry("last", LastCommand.class),
            entry("shuffle", ShuffleCommand.class),
            entry("loop", LoopCommand.class),
            entry("seek", SeekCommand.class),
            entry("lyrics", LyricsCommand.class)
    );

    public AudioModule() {
        super("audio", false, commands);
    }

}
