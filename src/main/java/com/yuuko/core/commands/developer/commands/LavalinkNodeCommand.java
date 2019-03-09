package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.developer.DeveloperModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.net.URI;

public class LavalinkNodeCommand extends Command {

    public LavalinkNodeCommand() {
        super("lavalink", DeveloperModule.class, 2, new String[]{"-lavalink <action> <node> <secret>"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 3);
            if(commandParameters[0].equals("add")) {
                Configuration.LAVALINK.getLavalink().addNode(URI.create(commandParameters[1]), commandParameters[2]);
            } else if(commandParameters[0].equals("remove")) {
                Configuration.LAVALINK.getLavalink().removeNode(Integer.parseInt(commandParameters[1]));
            }
        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }
    }
}
