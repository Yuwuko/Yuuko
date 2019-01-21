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
        String channel = e.getChannel().getId();

        if(channel != null) {
            ModuleBindFunctions.cleanupBinds(channel);

            if(channel.equals(DatabaseFunctions.getGuildSetting("starboard", channel))) {
                DatabaseFunctions.cleanupSettings("starboard", e.getGuild().getId());
                return;
            }

            if(channel.equals(DatabaseFunctions.getGuildSetting("commandLog", channel))) {
                DatabaseFunctions.cleanupSettings("commandLog", e.getGuild().getId());
                return;
            }

            if(channel.equals(DatabaseFunctions.getGuildSetting("newMember", channel))) {
                DatabaseFunctions.cleanupSettings("newMember", e.getGuild().getId());
            }
        }
    }

}
