package com.yuuko.core.modules.audio;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.audio.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleAudio extends Module {

    public ModuleAudio(MessageReceivedEvent e, String[] command) {
        super("Audio", "moduleAudio", false, new Command[]{
                new CommandPlay(),
                new CommandPause(),
                new CommandStop(),
                new CommandSearch(),
                new CommandSkip(),
                new CommandClear(),
                new CommandQueue(),
                new CommandBackground(),
                new CommandCurrent(),
                new CommandLast(),
                new CommandShuffle(),
                new CommandRepeat()
        });

        new CommandExecutor(e, command, this);
    }

}
