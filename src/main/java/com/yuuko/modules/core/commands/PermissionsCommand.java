package com.yuuko.modules.core.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class PermissionsCommand extends Command {

    public PermissionsCommand() {
        super("permissions", Arrays.asList("-permissions"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        String permissions = context.getGuild().getSelfMember().getPermissions().toString().replace("[", "").replace("]", "").replace(",", "\n");
        EmbedBuilder about = new EmbedBuilder().setTitle(context.i18n( "title"))
                .setDescription(context.i18n( "desc"))
                .addField(context.i18n( "granted"), permissions, true);
        MessageDispatcher.reply(context, about.build());
    }
}
