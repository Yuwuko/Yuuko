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
            EmbedBuilder embed = new EmbedBuilder().setTitle("Events")
                    .setDescription((channel == null) ? "There is currently no event channel." : "The event channel is currently set to " + context.getGuild().getTextChannelById(channel).getAsMention())
                    .addField("Help", "Use `" + context.getPrefix() + "help " + context.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(context.getParameters().equalsIgnoreCase("setup")) {
            context.getGuild().createTextChannel("events").queue(channel -> {
                channel.createPermissionOverride(context.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("eventchannel", channel.getId(), context.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Events").setDescription("The " + channel.getAsMention() + " channel has been setup correctly.");
                    MessageDispatcher.reply(context, embed.build());
                }
            });
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(context);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("eventchannel", channel.getId(), context.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Events").setDescription("The event channel has been set to " + channel.getAsMention() + ".");
                MessageDispatcher.reply(context, embed.build());
            }
            return;
        }

        if(GuildFunctions.setGuildSettings("eventchannel", null, context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Events").setDescription("The events channel has been unset.");
            MessageDispatcher.reply(context, embed.build());
        }
    }
}
