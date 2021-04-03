package com.yuuko.modules.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.ApiManager;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class ReloadApiCommand extends Command {

    public ReloadApiCommand() {
        super("reloadapi", 0, -1L, Arrays.asList("-reapi"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        Yuuko.API_MANAGER = new ApiManager();
        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "success"));
        MessageDispatcher.reply(context, embed.build());
    }
}
