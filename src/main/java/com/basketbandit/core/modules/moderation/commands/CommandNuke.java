package com.basketbandit.core.modules.moderation.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.List;

public class CommandNuke extends Command {

    public CommandNuke() {
        super("nuke", "com.basketbandit.core.modules.moderation.ModuleModeration",1, new String[]{"-nuke [value]"}, Permission.MESSAGE_MANAGE);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        int value = Integer.parseInt(command[1]);

        if(value < 1 || value > 100) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Input must be a positive value between **1** and **100**.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        List<Message> nukeList = e.getTextChannel().getHistory().retrievePast(value).complete();

        if(value < 2) {
            e.getTextChannel().deleteMessageById(nukeList.get(0).getId()).complete();
        } else {
            // If a message in the nuke list is older than 2 weeks it can't be mass deleted, so recursion will need to take place.
            for(Message message: nukeList) {
                if(message.getCreationTime().isBefore(OffsetDateTime.now().minusWeeks(2))) {
                    message.delete().complete();
                }
            }
            e.getGuild().getTextChannelById(e.getTextChannel().getId()).deleteMessages(nukeList).complete();
        }
    }

}
