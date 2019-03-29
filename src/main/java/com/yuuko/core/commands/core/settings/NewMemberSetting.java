package com.yuuko.core.commands.core.settings;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.GuildFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class NewMemberSetting extends Setting {

    public NewMemberSetting(MessageEvent e) {
        onCommand(e);
    }

    protected void onCommand(MessageEvent e) {
        String[] parameters = e.getCommand()[1].split("\\s+", 3);

        // If "!settings newMember message"
        if(parameters[1].equalsIgnoreCase("message")) { //
            // Guarding against invalid inputs.
            if(parameters.length < 3 || parameters[2].length() > 255 || parameters[2].length() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("The welcome message cannot be longer than `255`, or shorter than `1` characters.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            // If "!settings newMember message reset"
            if(parameters[2].equalsIgnoreCase("reset")) {
                if(GuildFunctions.setGuildSettings("newMemberMessage", null, e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Message").setDescription("The welcome message has reset to the default message.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            } else {
                if(GuildFunctions.setGuildSettings("newMemberMessage", parameters[2], e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Message").setDescription("The welcome message has been set to `" + parameters[2] + "`");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
            return;
        }

        // If "!settings newMember unset"
        if(parameters[1].equalsIgnoreCase("unset")) {
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
