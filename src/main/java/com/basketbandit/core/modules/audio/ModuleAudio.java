package com.basketbandit.core.modules.audio;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.audio.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleAudio extends Module {

    public ModuleAudio(MessageReceivedEvent e, String[] command) {
        super("ModuleAudio", "moduleAudio", new Command[]{
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

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                new CommandExecutor(e, command, this);
            }
        }

    }

}
