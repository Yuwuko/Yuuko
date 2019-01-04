package com.yuuko.core.modules.utility.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.utility.UtilityModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.format.DateTimeFormatter;

public class ServerCommand extends Command {

    public ServerCommand() {
        super("server", UtilityModule.class, 0, new String[]{"-server"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        Guild server = e.getGuild();
        StringBuilder emoteString = new StringBuilder();

        if(!server.getEmoteCache().isEmpty()) {
            int characterCount = 0;

            for(Emote emote : server.getEmoteCache()) {
                if(characterCount + emote.getAsMention().length() + 1 < 1024) {
                    emoteString.append(emote.getAsMention()).append(" ");
                    characterCount += emote.getAsMention().length() + 1;
                }
            }
        } else {
            emoteString.append("None Available");
        }

        EmbedBuilder commandInfo = new EmbedBuilder()
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
                .addField("Emotes", emoteString.toString(), false)
                .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
        MessageHandler.sendMessage(e, commandInfo.build());
    }

}
