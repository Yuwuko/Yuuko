package com.yuuko.core.commands.core.settings;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Setting {
    protected static final Logger log = LoggerFactory.getLogger(Setting.class);

    // Abstract method signature to ensure method is implemented.
    protected abstract void onCommand(MessageReceivedEvent e, String command);
}
