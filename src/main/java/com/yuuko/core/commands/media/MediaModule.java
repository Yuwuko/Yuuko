package com.yuuko.core.commands.media;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.media.commands.GithubCommand;
import com.yuuko.core.commands.media.commands.KitsuCommand;
import com.yuuko.core.commands.media.commands.OsuCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MediaModule extends Module {
    private static final Command[] commands = new Command[] {
            new KitsuCommand(),
            new OsuCommand(),
            new GithubCommand()
            //new RedditCommand()
    };

    public MediaModule(MessageReceivedEvent e, String[] command) {
        super("Media", "moduleMedia", false, commands);
        new CommandExecutor(e,this, command);
    }

}
