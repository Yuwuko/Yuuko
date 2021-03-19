package com.yuuko.commands.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.ApiManager;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class ReloadApiCommand extends Command {

    public ReloadApiCommand() {
        super("reloadapi", Yuuko.MODULES.get("developer"), 0, -1L, Arrays.asList("-reapi"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        Yuuko.API_MANAGER = new ApiManager();
        EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully reloaded ApiManager.");
        MessageDispatcher.reply(e, embed.build());
    }
}
