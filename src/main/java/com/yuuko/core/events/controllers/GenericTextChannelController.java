package com.yuuko.core.events.controllers;

import com.yuuko.core.commands.core.commands.BindCommand;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;

public class GenericTextChannelController {

    public GenericTextChannelController(GenericTextChannelEvent e) {
        if(e instanceof TextChannelDeleteEvent) {
            textChannelDeleteEvent((TextChannelDeleteEvent)e);
        }
    }

    private void textChannelDeleteEvent(TextChannelDeleteEvent e) {
        BindCommand.DatabaseInterface.cleanupReferences(e.getChannel().getId());
    }
}

