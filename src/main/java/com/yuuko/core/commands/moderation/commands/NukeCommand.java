package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.ModerationLogSetting;
import com.yuuko.core.commands.moderation.ModerationModule;
import com.yuuko.core.database.function.BindFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public class NukeCommand extends Command {

    public NukeCommand() {
        super("nuke", ModerationModule.class,1, Arrays.asList("-nuke <value>", "-nuke #channel"), false, Arrays.asList(Permission.MESSAGE_MANAGE, Permission.MANAGE_CHANNEL));
    }

    @Override
    public void onCommand(MessageEvent e) {
        List<TextChannel> channels = e.getMessage().getMentionedChannels();
        if(channels.size() > 0 && channels.size() < 6) {
            channels.forEach(channel -> {
                BindFunctions.cleanupBinds(channel.getId());
                channel.createCopy().queue(r -> channel.delete().queue());
            });
            return;
        }

        if(!Sanitiser.isNumber(e.getCommand().get(1))) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Input must be a positive integer between **2** and **99** or a channel, e.g. #general.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        final int value = Integer.parseInt(e.getCommand().get(1));

        if(value < 2 || value > 99) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Input must be a positive integer between **2** and **99** or a channel, e.g. #general.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // Filter out old messages from the mass delete list.
        final OffsetDateTime past = OffsetDateTime.now().minusWeeks(2);
        e.getChannel().getHistory().retrievePast(value+1).queue(messages -> {
            messages.listIterator().forEachRemaining(message -> {
                if(message != null && message.getCreationTime().isBefore(past)) {
                    message.delete().queue();
                }
            });

            if(messages.size() > 1) {
                e.getGuild().getTextChannelById(e.getChannel().getId()).deleteMessages(messages.subList(1, messages.size())).queue(s -> {
                    ModerationLogSetting.execute(e, messages.size()); // Attempt to add event to moderation log.
                });
            }
        });
    }

}
