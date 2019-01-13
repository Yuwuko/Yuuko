package com.yuuko.core.modules.media;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.media.commands.KitsuCommand;
import com.yuuko.core.modules.media.commands.OsuCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MediaModule extends Module {

    public MediaModule(MessageReceivedEvent e, String[] command) {
        super("Media", "moduleMedia", false, new Command[]{
                new KitsuCommand(),
                new OsuCommand()
        });

        new CommandExecutor(e,this, command);
    }

}
