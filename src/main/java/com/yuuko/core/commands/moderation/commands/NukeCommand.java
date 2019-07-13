package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.ModerationLogSetting;
import com.yuuko.core.commands.moderation.ModerationModule;
import com.yuuko.core.database.function.BindFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public class NukeCommand extends Command {

    public NukeCommand() {
        super("nuke", ModerationModule.class,1, Arrays.asList("-nuke <value>", "-nuke #channel"), false, Arrays.asList(Permission.MESSAGE_MANAGE, Permission.MANAGE_CHANNEL, Permission.MESSAGE_HISTORY));
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

        // Checks length of parameters since the command doesn't take a value greater than 3 digits
        // Also prevents NumberFormatException for parsing the integer later.
        if(e.getParameters().length() > 3 || !Sanitiser.isNumber(e.getParameters())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Input must be a positive integer between **2** and **100** or a channel, e.g. #general.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        int value = Integer.parseInt(e.getParameters());

        if(value < 2) {
            value = 2;
        } else if(value > 99) {
            value = 99;
        }

        // Filter out old messages from the mass delete list.
        final OffsetDateTime past = OffsetDateTime.now().minusWeeks(2);
        e.getChannel().getHistory().retrievePast(value+1).queue(messages -> {
            messages.listIterator().forEachRemaining(message -> {
                if(message != null && message.getCreationTime().isBefore(past)) {
                    message.delete().queue(s -> {}, f -> log.warn("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), f.getMessage(), f));
                }
            });

            // Ensures messages size is 3 or greater. (2 + nuke message)
            if(messages.size() > 2) {
                e.getChannel().deleteMessages(messages.subList(1, messages.size())).queue(s -> {
                    ModerationLogSetting.execute(e, messages.size()); // Attempt to add event to moderation log.
                }, f -> log.warn("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), f.getMessage(), f));
            }
        });
    }

}
