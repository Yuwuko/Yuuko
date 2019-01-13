package com.yuuko.core.modules.world;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.world.commands.LineStatusCommand;
import com.yuuko.core.modules.world.commands.TescoCommand;
import com.yuuko.core.modules.world.commands.WeatherCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WorldModule extends Module {

    public WorldModule(MessageReceivedEvent e, String[] command) {
        super("World", "moduleWorld", false, new Command[]{
                new LineStatusCommand(),
                new WeatherCommand(),
                new TescoCommand()
        });

        new CommandExecutor(e,this, command);
    }

}
