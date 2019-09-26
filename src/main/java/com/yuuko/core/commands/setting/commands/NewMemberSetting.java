package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;

public class NewMemberSetting extends Command {

    public NewMemberSetting() {
        super("newmember", Configuration.MODULES.get("setting"), 0, Arrays.asList("-newmember", "-newmember <message>", "-newmember reset", "-newmember unset", "-newmember <#channel>", "-newmember autoremove"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) {
        if(!e.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("newmember", e.getGuild().getId());
            String status = (channel == null) ? "There is currently no new member channel set." : "The moderation new member channel is currently " + e.getGuild().getTextChannelById(channel).getAsMention();
            String message = GuildFunctions.getGuildSetting("newMemberMessage", e.getGuild().getId());

            EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription(status)
                    .addField("Message", (message == null) ? "Welcome to **%guild%**, %user%!" : message, false)
                    .addField("Auto Remove", GuildFunctions.getGuildSettingBoolean("newMemberRemoveMessage",  e.getGuild().getId()) ? "True" : "False", true)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", false);
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        String[] parameters = e.getParameters().split("\\s+", 3);

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

        // If "!settings newMember autoremove"
        if(parameters[1].equalsIgnoreCase("autoremove")) {
            String flippedPosition = GuildFunctions.getGuildSettingBoolean("newMemberRemoveMessage",  e.getGuild().getId()) ? "0" : "1";
            if(GuildFunctions.setGuildSettings("newMemberRemoveMessage", flippedPosition, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Auto Remove Messages").setDescription("Auto remove new member messages has now been set to " + (flippedPosition.equals("1") ? "`true`." : "`false`."));
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
