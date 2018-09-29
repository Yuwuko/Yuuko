package com.basketbandit.core.modules.utility.commands;

import com.basketbandit.core.modules.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class CommandWeather extends Command {

    public CommandWeather() {
        super("weather", "com.basketbandit.core.modules.utility.ModuleUtility", new String[]{"-weather [city]", "-weather [city] [country]"}, null);
    }

    public CommandWeather(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {

    }
}
