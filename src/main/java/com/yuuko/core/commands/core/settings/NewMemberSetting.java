package com.yuuko.core.commands.core.settings;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class NewMemberSetting {

    public NewMemberSetting(MessageReceivedEvent e) {
        executeCommand(e);
    }

    private void executeCommand(MessageReceivedEvent e) {
        TextChannel channel = MessageUtility.getFirstMentionedChannel(e);
        if(channel != null) {
            if(DatabaseFunctions.setGuildSettings("newMember", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("New Member").setDescription("The welcome channel has been set to **" + channel.getAsMention() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            if(DatabaseFunctions.setGuildSettings("newMember", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("New Member").setDescription("The welcome channel has been unset, thus deactivating the greeting of new members.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }
}
