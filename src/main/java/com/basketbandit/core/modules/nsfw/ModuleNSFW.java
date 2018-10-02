package com.basketbandit.core.modules.nsfw;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.nsfw.commands.CommandEfukt;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleNSFW extends Module {

    public ModuleNSFW(MessageReceivedEvent e, String[] command) {
        super("ModuleNSFW", "moduleNSFW", new Command[] {
                new CommandEfukt()
        });

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                if(!isNSFW(e)) {
                    Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", this command can only be used in NSFW flagged channels.");
                    return;
                }
                new CommandExecutor(e, command, this);
            }
        }
    }

}
