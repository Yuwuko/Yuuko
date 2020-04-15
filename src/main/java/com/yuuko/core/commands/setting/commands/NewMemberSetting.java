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
    private MessageEvent e;
    private String[] parameters;

    public NewMemberSetting() {
        super("newmember", Configuration.MODULES.get("setting"), 0, Arrays.asList("-newmember", "-newmember <message>", "-newmember reset", "-newmember unset", "-newmember <#channel>", "-newmember autoremove"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) {
        if(!e.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("newmember", e.getGuild().getId());
            String status = (channel == null) ? "There is currently no new member notification channel set." : "The new member notification channel is currently " + e.getGuild().getTextChannelById(channel).getAsMention();
            String message = GuildFunctions.getGuildSetting("newMemberMessage", e.getGuild().getId());

            EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription(status)
                    .addField("Message", (message == null) ? "Welcome to **%guild%**, %user%!" : message, false)
                    .addField("Auto Remove", GuildFunctions.getGuildSettingBoolean("newMemberRemoveMessage",  e.getGuild().getId()) ? "True" : "False", true)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", false);
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        this.e = e;
        this.parameters = e.getParameters().split("\\s+", 3);

        switch(parameters[1].toLowerCase()) {
            case "autoremove" -> toggleAutoRemove();
            case "message" -> setMessage();
            case "unset" -> unsetChannel();
            default -> setChannel();
        }
    }

    /**
     * Sets channel for NewMember setting, enabling future execution.
     */
    private void setChannel() {
        // If "!settings newMember #Channel"
        TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("newMember", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Channel").setDescription("The welcome channel has been set to **" + channel.getAsMention() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    /**
     * Unset NewMember channel, stopping future execution.
     */
    private void unsetChannel() {
        if(GuildFunctions.setGuildSettings("newMember", null, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Channel").setDescription("The welcome channel has been unset, deactivating the greeting of new members.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

    /**
     * Sets or resets the NewMember message, depending on parameters.
     */
    private void setMessage() {
        // Guarding against invalid inputs.
        if(parameters.length < 3 || parameters[2].length() > 255 || parameters[2].length() < 1) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("The welcome message cannot be longer than `255`, or shorter than `1` character.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // If reset parameter is set
        if(parameters[2].equalsIgnoreCase("reset")) {
            if(GuildFunctions.setGuildSettings("newMemberMessage", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Message").setDescription("The welcome message has reset to the default message.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;
        }

        if(GuildFunctions.setGuildSettings("newMemberMessage", parameters[2], e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Message").setDescription("The welcome message has been set to `" + parameters[2] + "`");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

    /**
     * Toggles auto-remove feature. (removes message after 1 minute)
     */
    private void toggleAutoRemove() {
        String flippedPosition = GuildFunctions.getGuildSettingBoolean("newMemberRemoveMessage",  e.getGuild().getId()) ? "0" : "1";
        if(GuildFunctions.setGuildSettings("newMemberRemoveMessage", flippedPosition, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("New Member - Auto Remove Messages").setDescription("Auto remove new member messages has now been set to " + (flippedPosition.equals("1") ? "`true`." : "`false`."));
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
