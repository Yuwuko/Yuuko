package com.yuuko.core.commands.core.settings;

import com.yuuko.core.database.GuildFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class NewMemberSetting {

    public NewMemberSetting(MessageReceivedEvent e, String value) {
        onCommand(e, value);
    }

    private void onCommand(MessageReceivedEvent e, String value) {
        String[] values = value.split("\\s+", 2);

        // If "!settings newMember message"
        if(values[0].equalsIgnoreCase("message")) { //

            // Guarding against invalid inputs.
            if(values.length < 2 || values[1].length() > 255 || values[1].length() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("The welcome message cannot be longer than 255, or shorter than 1 characters.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            // If "!settings newMember message reset"
            if(values[1].equalsIgnoreCase("reset")) {
                if(GuildFunctions.setGuildSettings("newMemberMessage", null, e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Message").setDescription("The welcome message has reset to the default message.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            } else {
                if(GuildFunctions.setGuildSettings("newMemberMessage", values[1], e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Message").setDescription("The welcome message has been set to `" + values[1] + "`");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
            return;
        }

        // If "!settings newMember unset"
        if(values[0].equalsIgnoreCase("unset")) {
            if(GuildFunctions.setGuildSettings("newMember", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Channel").setDescription("The welcome channel has been unset, deactivating the greeting of new members.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;
        }

        // If "!settings newMember #Channel"
        TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("newMember", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Channel").setDescription("The welcome channel has been set to **" + channel.getAsMention() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

}
