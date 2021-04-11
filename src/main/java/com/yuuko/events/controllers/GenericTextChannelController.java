package com.yuuko.events.controllers;

import com.yuuko.modules.core.commands.BindCommand;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;

public class GenericTextChannelController {

    public GenericTextChannelController(GenericTextChannelEvent event) {
        if(event instanceof TextChannelDeleteEvent) {
            textChannelDeleteEvent((TextChannelDeleteEvent)event);
        }
    }

    private void textChannelDeleteEvent(TextChannelDeleteEvent event) {
        BindCommand.DatabaseInterface.cleanupReferences(event.getChannel().getId());
    }
}

