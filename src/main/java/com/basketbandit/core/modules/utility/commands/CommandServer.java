package com.basketbandit.core.modules.utility.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class CommandServer extends Command {

    public CommandServer() {
        super("server", "com.basketbandit.core.modules.utility.ModuleUtility", 0, new String[]{"-server"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        Guild server = e.getGuild();
        String emotes = "";

        if(!server.getEmoteCache().isEmpty()) {
            int characterCount = 0;
            for(Emote emote : server.getEmoteCache()) {
                if(characterCount + emote.getAsMention().length() + 1 < 1024) {
                    emotes += emote.getAsMention() + " ";
                    characterCount += emote.getAsMention().length() + 1;
                }
            }
        } else {
            emotes = "None Available";
        }

        EmbedBuilder commandInfo = new EmbedBuilder()
                .setColor(Color.DARK_GRAY)
                .setTitle("Server information for: " + server.getName(), null)
                .setThumbnail(server.getIconUrl())
                .addField("Owner", server.getOwner().getUser().getName() + "#" + server.getOwner().getUser().getDiscriminator(), true)
                .addField("ID", server.getId(), true)
                .addField("Created", server.getCreationTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mma")), true)
                .addField("Region", server.getRegion().getName(), true)
                .addField("Users", server.getMemberCache().size()+"", true)
                .addField("Text Channels", server.getTextChannelCache().size()+"", true)
                .addField("Voice Channels", server.getVoiceChannels().size()+"", true)
                .addField("Roles", server.getRoles().size()+"", true)
                .addField("Emotes", emotes, false)
                .setFooter(Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

        Utils.sendMessage(e, commandInfo.build());
    }

}
