package com.yuuko.core.modules.nsfw;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.nsfw.commands.CommandEfukt;
import com.yuuko.core.modules.nsfw.commands.CommandNeko;
import com.yuuko.core.utils.MessageHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleNSFW extends Module {

    public ModuleNSFW(MessageReceivedEvent e, String[] command) {
        super("ModuleNSFW", "moduleNSFW", new Command[] {
                new CommandEfukt(),
                new CommandNeko()
        });

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                if(!isNSFW(e)) {
                    MessageHandler.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", this command can only be used in NSFW flagged channels.");
                    return;
                }
                new CommandExecutor(e, command, this);
            }
        }

    }

}
