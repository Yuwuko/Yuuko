package com.yuuko.core.controllers;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.SystemClock;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.core.commands.CommandSetup;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.List;

public class GenericGuildController {

    public GenericGuildController(GenericGuildEvent e) {
        if(e instanceof GuildJoinEvent) {
            guildJoinEvent((GuildJoinEvent)e);
        } else if(e instanceof GuildLeaveEvent) {
            guildLeaveEvent((GuildLeaveEvent)e);
        }
    }

    private void guildJoinEvent(GuildJoinEvent e) {
        new CommandSetup().executeAutomated(e);

        Cache.GUILD_COUNT = Cache.JDA.getGuilds().size();

        EmbedBuilder about = new EmbedBuilder()
                .setAuthor(Cache.BOT.getName() + "#" + Cache.BOT.getDiscriminator(), null, Cache.BOT.getAvatarUrl())
                .setDescription("Automatic setup was successful! Thanks for inviting me to your server, below is information about myself. Commands can be found [here](https://github.com/BasketBandit/Yuuko-Java)! If you have any problems, suggestions, or general feedback, please join the (support server)[https://discord.gg/QcwghsA] and let yourself be known!")
                .setThumbnail(Cache.BOT.getAvatarUrl())
                .addField("Author", "[0x00000000#0001](https://github.com/BasketBandit/)", true)
                .addField("Version", Configuration.VERSION, true)
                .addField("Servers", Cache.GUILD_COUNT + "", true)
                .addField("Commands", Cache.COMMANDS.size() + "", true)
                .addField("Invocation", Configuration.GLOBAL_PREFIX, true)
                .addField("Uptime", SystemClock.getRuntimeString(), true)
                .addField("Ping", Cache.PING + "", true);

        List<TextChannel> channels = e.getGuild().getTextChannels();

        for(TextChannel c: channels) {
            if(c.getName().toLowerCase().contains("general") || c.getName().toLowerCase().contains("primary")) {
                try {
                    MessageHandler.sendMessage(c, about.build());
                    break;
                } catch(PermissionException ex) {
                    Utils.updateLatest("[INFO] Server disallowed message to be sent to general - " + e.getGuild().getName() + " (" + e.getGuild().getId() + ")");
                }
            }
        }

        Utils.updateDiscordBotList();
        Utils.updateLatest("[INFO] Joined new server: " + e.getGuild().getName() + " (Id: " + e.getGuild().getIdLong() + ", Users: " + e.getGuild().getMemberCache().size() + ")");
    }

    private void guildLeaveEvent(GuildLeaveEvent e) {
        new DatabaseFunctions().cleanup(e.getGuild().getId());

        Cache.GUILD_COUNT = Cache.JDA.getGuilds().size();

        Utils.updateDiscordBotList();
        Utils.updateLatest("[INFO] Left server: " + e.getGuild().getName() + " (" + e.getGuild().getIdLong() + ")");
    }
}
