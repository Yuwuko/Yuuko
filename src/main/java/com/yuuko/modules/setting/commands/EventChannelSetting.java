package com.yuuko.modules.setting.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;

public class EventChannelSetting extends Command {

    public EventChannelSetting() {
        super("eventchannel", 1, -1L, Arrays.asList("-eventchannel <#channel>", "-eventchannel setup", "-eventchannel unset"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("eventchannel", context.getGuild().getId());
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("title"))
                    .setDescription((channel == null) ? context.i18n("not_set") : context.i18n("set").formatted(context.getGuild().getTextChannelById(channel).getAsMention()))
                    .addField(context.i18n("help"), context.i18n("help_desc").formatted(context.getPrefix(), context.getCommand().getName()), false);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(context.getParameters().equalsIgnoreCase("setup")) {
            context.getGuild().createTextChannel("events").queue(channel -> {
                channel.createPermissionOverride(context.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("eventchannel", channel.getId(), context.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(context.i18n("title"))
                            .setDescription(context.i18n("setup").formatted(channel.getAsMention()));
                    MessageDispatcher.reply(context, embed.build());
                }
            });
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(context);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("eventchannel", channel.getId(), context.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("title"))
                        .setDescription(context.i18n("set_success").formatted(channel.getAsMention()));
                MessageDispatcher.reply(context, embed.build());
            }
            return;
        }

        if(GuildFunctions.setGuildSettings("eventchannel", null, context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("title"))
                    .setDescription(context.i18n("unset_success"));
            MessageDispatcher.reply(context, embed.build());
        }
    }
}
