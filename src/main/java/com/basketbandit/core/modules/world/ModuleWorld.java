package com.basketbandit.core.modules.world;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.world.commands.CommandLineStatus;
import com.basketbandit.core.modules.world.commands.CommandWeather;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleWorld extends Module {

    public ModuleWorld(MessageReceivedEvent e, String[] command) {
        super("ModuleWorld", "moduleWorld", new Command[]{
                new CommandLineStatus(),
                new CommandWeather()
        });

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                new CommandExecutor(e, command, this);
            }
        }
    }

}
