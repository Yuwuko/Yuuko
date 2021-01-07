package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.api.ApiManager;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class ReloadApiCommand extends Command {

    public ReloadApiCommand() {
        super("reloadapi", Config.MODULES.get("developer"), 0, -1L, Arrays.asList("-reapi"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        Config.API_MANAGER = new ApiManager();
        EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully reloaded ApiManager.");
        MessageDispatcher.reply(e, embed.build());
    }
}
