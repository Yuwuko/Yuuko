package com.yuuko.core.commands.core.settings;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StarboardSetting {

    public StarboardSetting(MessageReceivedEvent e, String value) {
        executeCommand(e, value);
    }

    public void executeCommand(MessageReceivedEvent e, String value) {
        if(value.equalsIgnoreCase("setup")) {
            if(e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS)) {
                setupStarboard(e);
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the **Manage Channel** and **Manage Permissions** permissions to setup the starboard automatically.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;
        }

        TextChannel channel = MessageUtility.getFirstMentionedChannel(e);
        if(channel != null) {
            if(DatabaseFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been set to **" + channel.getName() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            if(DatabaseFunctions.setGuildSettings("starboard", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been unset, thus deactivating the starboard.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    private void setupStarboard(MessageReceivedEvent e) {
        try {
            e.getGuild().getController().createTextChannel("starboard").queue(channel -> {
                channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE).queue();
                if(DatabaseFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The **starboard** channel has been setup correctly.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            });
        } catch(Exception ex) {
            //
        }
    }
}
