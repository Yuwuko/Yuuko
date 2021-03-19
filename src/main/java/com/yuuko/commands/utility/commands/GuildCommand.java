package com.yuuko.commands.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class GuildCommand extends Command {

    public GuildCommand() {
        super("guild", Yuuko.MODULES.get("utility"), 0, -1L, Arrays.asList("-guild"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        Guild guild = e.getGuild();
        StringBuilder emoteString = new StringBuilder();

        if(!guild.getEmoteCache().isEmpty()) {
            int characterCount = 0;
            for(Emote emote : guild.getEmoteCache()) {
                if(characterCount + emote.getAsMention().length() + 1 < 1024) {
                    emoteString.append(emote.getAsMention()).append(" ");
                    characterCount += emote.getAsMention().length() + 1;
                }
            }
        } else {
            emoteString.append("None Available");
        }

        guild.retrieveOwner().queue(s -> {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Guild information for: " + guild.getName(), null)
                    .setThumbnail(guild.getIconUrl())
                    .addField("Owner", s.getUser().getName() + "#" + s.getUser().getDiscriminator(), true)
                    .addField("ID", guild.getId(), true)
                    .addField("Created", guild.getTimeCreated().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mma")), true)
                    .addField("Region", guild.getRegion().getName(), true)
                    .addField("Users", "Unknown (Missing Intent)", true)
                    .addField("Text Channels", guild.getTextChannelCache().size()+"", true)
                    .addField("Voice Channels", guild.getVoiceChannelCache().size()+"", true)
                    .addField("Roles", guild.getRoles().size()+"", true)
                    .addField("Emotes", emoteString.toString(), false)
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), e.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(e, embed.build());
        });
    }

}
