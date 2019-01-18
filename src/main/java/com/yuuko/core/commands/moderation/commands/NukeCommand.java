package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.moderation.ModerationModule;
import com.yuuko.core.database.ModuleBindFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.List;

public class NukeCommand extends Command {

    public NukeCommand() {
        super("nuke", ModerationModule.class,1, new String[]{"-nuke [value]", "-nuke #channel"}, new Permission[]{Permission.MESSAGE_MANAGE, Permission.MANAGE_CHANNEL});
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        List<TextChannel> channels = e.getMessage().getMentionedChannels();
        if(channels.size() > 0 && channels.size() < 11) {
            channels.forEach(channel -> {
                ModuleBindFunctions.cleanupBinds(channel.getId());
                channel.createCopy().queue((r) -> channel.delete().queue());
            });
            return;
        }

        if(Sanitiser.isNumber(command[1])) {
            final int value = Integer.parseInt(command[1]);

            if(value < 2 || value > 100) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Input must be a positive integer between **2** and **100** or a channel, e.g. #general.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            // Filter out old messages from the mass delete list.
            List<Message> nukeList = e.getTextChannel().getHistory().retrievePast(value).complete();
            Iterator<Message> it = nukeList.iterator();
            while(it.hasNext()) {
                Message message = it.next();
                if(message.getCreationTime().isBefore(OffsetDateTime.now().minusWeeks(2))) {
                    message.delete().queue();
                    it.remove();
                }
            }

            if(nukeList.size() > 1) {
                e.getGuild().getTextChannelById(e.getTextChannel().getId()).deleteMessages(nukeList).queue();
            }

        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Input must be a positive integer between **2** and **100** or a tagged channel, e.g. **#general**.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
