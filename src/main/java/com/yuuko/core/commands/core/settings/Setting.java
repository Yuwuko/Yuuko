package com.yuuko.core.commands.core.settings;

import com.yuuko.core.events.entity.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Setting {
    protected static final Logger log = LoggerFactory.getLogger(Setting.class);

    // Abstract method signature to ensure method is implemented.
    protected abstract void onCommand(MessageEvent e);
}
