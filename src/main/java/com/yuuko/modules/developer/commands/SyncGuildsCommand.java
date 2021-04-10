package com.yuuko.modules.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.core.commands.BindCommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SyncGuildsCommand extends Command {

    public SyncGuildsCommand() {
        super("syncguilds", 0, -1L, Arrays.asList("-syncguilds"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        context.getJDA().getGuildCache().forEach(guild -> {
            GuildFunctions.addGuild(guild);
            BindCommand.DatabaseInterface.verifyBinds(guild);
        });
        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "success"));
        MessageDispatcher.reply(context, embed.build());
    }

}
