package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.setting.commands.ModerationLogSetting;
import com.yuuko.core.database.function.BindFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
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
        super("nuke", Configuration.MODULES.get("moderation"),1, Arrays.asList("-nuke <value>", "-nuke #channel"), false, Arrays.asList(Permission.MESSAGE_MANAGE, Permission.MANAGE_CHANNEL, Permission.MESSAGE_HISTORY));
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
        value = value < 2 ? 2 : Math.min(value, 99);

        e.getChannel().getHistory().retrievePast(value+1).queue(messages -> {
            // Beautiful solution using Collectors.partitionBy() to generate 2 lists based on a boolean comparison.
            OffsetDateTime past = OffsetDateTime.now().minusWeeks(2);
            Map<Boolean, List<Message>> sortedMessages = messages.stream().collect(Collectors.partitioningBy(message -> message.getTimeCreated().isBefore(past)));

            List<Message> oldMessages = sortedMessages.get(true);
            oldMessages.listIterator().forEachRemaining(message -> {
                message.delete().queue(s -> {}, f -> log.warn("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), f.getMessage(), f));
            });

            // Mass deletion requires a list of at least size 2, I chose 3 to encompass the invoking command also.
            List<Message> newMessages = sortedMessages.get(false);
            if(newMessages.size() > 2) {
                e.getChannel().deleteMessages(newMessages.subList(1, newMessages.size())).queue(s -> {
                    ModerationLogSetting.execute(e, messages.size()); // Attempt to add event to moderation log.
                }, f -> log.warn("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), f.getMessage(), f));
            }
        });
    }

}
