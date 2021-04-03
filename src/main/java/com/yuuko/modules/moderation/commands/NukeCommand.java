package com.yuuko.modules.moderation.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.setting.commands.ModerationLogSetting;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NukeCommand extends Command {

    public NukeCommand() {
        super("nuke",1, 10000L, Arrays.asList("-nuke <value>", "-nuke #channel"), false, Arrays.asList(Permission.MESSAGE_MANAGE, Permission.MANAGE_CHANNEL, Permission.MESSAGE_HISTORY));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        List<TextChannel> channels = context.getMessage().getMentionedChannels();
        if(channels.size() > 0) {
            TextChannel channel = channels.get(0);
            if(!channel.isNews()) {
                channel.createCopy().setPosition(channel.getPosition()).queue(r -> channel.delete().queue(s -> {}, f -> {}));
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Channel").setDescription("Channels marked as **news** cannot be nuked in this way.");
                MessageDispatcher.reply(context, embed.build());
            }
            return;
        }

        // Checks length of parameters since the command doesn't take a value greater than 3 digits
        // Also prevents NumberFormatException for parsing the integer later.
        if(context.getParameters().length() > 3 || !Sanitiser.isNumeric(context.getParameters())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Input must be a positive integer between **2** and **100** or a tagged channel. e.g. #general");
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        int value = Integer.parseInt(context.getParameters());
        value = value < 2 ? 2 : Math.min(value, 99);
        context.getChannel().getHistory().retrievePast(value+1).queue(messages -> {
            // Use Collectors.partitionBy() to generate 2 lists based on a boolean comparison of date.
            OffsetDateTime past = OffsetDateTime.now().minusWeeks(2);
            Map<Boolean, List<Message>> sortedMessages = messages.stream().collect(Collectors.partitioningBy(message -> message.getTimeCreated().isBefore(past)));

            // Removes messages that are valid for mass-deletion. (providing there are more than 2)
            if(sortedMessages.get(false).size() > 2) {
                context.getChannel().deleteMessages(sortedMessages.get(false).subList(1, sortedMessages.get(false).size())).queue(s -> {
                    ModerationLogSetting.execute(context, messages.size()); // Attempt to add event to moderation log.
                }, f -> {});
            }

            // Removes messages that are too old to be mass-deleted.
            sortedMessages.get(true).iterator().forEachRemaining(message -> message.delete().queue(s -> {}, f -> {}));
        });
    }

}
