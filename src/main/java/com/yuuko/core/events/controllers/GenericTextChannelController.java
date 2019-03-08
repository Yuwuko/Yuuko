package com.yuuko.core.events.controllers;

import com.yuuko.core.database.BindFunctions;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.database.GuildFunctions;
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
            BindFunctions.cleanupBinds(channel);

            if(channel.equals(GuildFunctions.getGuildSetting("starboard", channel))) {
                DatabaseFunctions.cleanupSettings("starboard", e.getGuild().getId());
                return;
            }

            if(channel.equals(GuildFunctions.getGuildSetting("commandLog", channel))) {
                DatabaseFunctions.cleanupSettings("commandLog", e.getGuild().getId());
                return;
            }

            if(channel.equals(GuildFunctions.getGuildSetting("newMember", channel))) {
                DatabaseFunctions.cleanupSettings("newMember", e.getGuild().getId());
            }
        }
    }

}
