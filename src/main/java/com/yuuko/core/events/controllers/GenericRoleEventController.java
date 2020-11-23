package com.yuuko.core.events.controllers;

import com.yuuko.core.commands.utility.commands.ReactionRoleCommand;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericRoleEventController {
    private static final Logger log = LoggerFactory.getLogger(GenericEmoteEventController.class);

    public GenericRoleEventController(GenericRoleEvent e) {
        if(e instanceof RoleDeleteEvent) {
            roleDeleteEvent((RoleDeleteEvent) e);
        }
    }

    public void roleDeleteEvent(RoleDeleteEvent e) {
        ReactionRoleCommand.DatabaseInterface.removeReactionRole(e.getRole());
    }
}
