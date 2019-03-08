package com.yuuko.core.commands.core.settings;

import com.yuuko.core.database.GuildFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class NewMemberSetting {

    public NewMemberSetting(MessageReceivedEvent e) {
        onCommand(e);
    }

    private void onCommand(MessageReceivedEvent e) {
        TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("newMember", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("New Member").setDescription("The welcome channel has been set to **" + channel.getAsMention() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            if(GuildFunctions.setGuildSettings("newMember", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("New Member").setDescription("The welcome channel has been unset, deactivating the greeting of new members.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }
}
