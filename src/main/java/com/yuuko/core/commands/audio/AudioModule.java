package com.yuuko.core.commands.audio;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.audio.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;

public class AudioModule extends Module {

    public static final HashMap<Long, List<SearchResult>> audioSearchResults = new HashMap<>();

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
                new LoopCommand()
        });

        new CommandExecutor(e,this, command);
    }

}
