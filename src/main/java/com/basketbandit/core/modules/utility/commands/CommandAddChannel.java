package com.basketbandit.core.modules.utility.commands;

import com.basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandAddChannel extends Command {

    public CommandAddChannel() {
        super("addchannel", "com.basketbandit.core.modules.utility.ModuleUtility", new String[]{"-addchannel [type] [name]", "-addchannel [type] [name] [nsfw]"}, Permission.MANAGE_CHANNEL);
    }

    public CommandAddChannel(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 2);
        String type = commandParameters[0].toLowerCase();

        if(type.equals("text")) {
            e.getGuild().getController().createTextChannel(commandParameters[1]).setNSFW(commandParameters.length > 2).queue();
        } else if(type.equals("voice")) {
            e.getGuild().getController().createVoiceChannel(commandParameters[1]).queue();
        }
    }


}
