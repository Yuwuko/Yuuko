package com.yuuko.core.modules.world;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.world.commands.CommandLineStatus;
import com.yuuko.core.modules.world.commands.CommandTesco;
import com.yuuko.core.modules.world.commands.CommandWeather;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleWorld extends Module {

    public ModuleWorld(MessageReceivedEvent e, String[] command) {
        super("World", "moduleWorld", false, new Command[]{
                new CommandLineStatus(),
                new CommandWeather(),
                new CommandTesco()
        });

        new CommandExecutor(e, command, this);
    }

}
