package com.yuuko.core.commands.moderation;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.moderation.commands.BanCommand;
import com.yuuko.core.commands.moderation.commands.KickCommand;
import com.yuuko.core.commands.moderation.commands.MuteCommand;
import com.yuuko.core.commands.moderation.commands.NukeCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModerationModule extends Module {

    public ModerationModule(MessageReceivedEvent e, String[] command) {
        super("Moderation", "moduleModeration", false, new Command[]{
                new NukeCommand(),
                new MuteCommand(),
                new BanCommand(),
                new KickCommand()
        });

        new CommandExecutor(e,this, command);
    }

}
