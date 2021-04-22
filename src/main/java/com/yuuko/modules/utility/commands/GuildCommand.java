package com.yuuko.modules.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class GuildCommand extends Command {

    public GuildCommand() {
        super("guild", Arrays.asList("-guild"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        Guild guild = context.getGuild();
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
            emoteString.append(context.i18n("none"));
        }

        guild.retrieveOwner().queue(s -> {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("title").formatted(guild.getName()), null)
                    .setThumbnail(guild.getIconUrl())
                    .addField(context.i18n("owner"), s.getUser().getName() + "#" + s.getUser().getDiscriminator(), true)
                    .addField(context.i18n("id"), guild.getId(), true)
                    .addField(context.i18n("created"), guild.getTimeCreated().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mma")), true)
                    .addField(context.i18n("region"), guild.getRegion().getName(), true)
                    .addField(context.i18n("users"), "Unknown (Missing Intent)", true)
                    .addField(context.i18n("text"), guild.getTextChannelCache().size()+"", true)
                    .addField(context.i18n("voice"), guild.getVoiceChannelCache().size()+"", true)
                    .addField(context.i18n("roles"), guild.getRoles().size()+"", true)
                    .addField(context.i18n("emotes"), emoteString.toString(), false)
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getName() + "#" + context.getAuthor().getDiscriminator(), context.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(context, embed.build());
        });
    }

}
