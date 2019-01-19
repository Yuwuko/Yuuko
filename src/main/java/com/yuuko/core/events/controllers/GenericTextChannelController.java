package com.yuuko.core.events.controllers;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.database.ModuleBindFunctions;
import net.dv8tion.jda.core.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;

public class GenericTextChannelController {

    public GenericTextChannelController(GenericTextChannelEvent e) {
        if(e instanceof TextChannelDeleteEvent) {
            textChannelDeleteEvent((TextChannelDeleteEvent)e);
        }
    }

    private void textChannelDeleteEvent(TextChannelDeleteEvent e) {
        ModuleBindFunctions.cleanupBinds(e.getChannel().getId());

        if(DatabaseFunctions.getGuildSetting("starboard", e.getGuild().getId()).equals(e.getChannel().getId())) {
            DatabaseFunctions.cleanupSettings("starboard", e.getGuild().getId());
            return;
        }

        if(DatabaseFunctions.getGuildSetting("commandLog", e.getGuild().getId()).equals(e.getChannel().getId())) {
            DatabaseFunctions.cleanupSettings("commandLog", e.getGuild().getId());
            return;
        }

        if(DatabaseFunctions.getGuildSetting("newMember", e.getGuild().getId()).equals(e.getChannel().getId())) {
            DatabaseFunctions.cleanupSettings("newMember", e.getGuild().getId());
        }

    }

}
