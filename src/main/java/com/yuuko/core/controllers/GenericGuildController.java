package com.yuuko.core.controllers;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.Statistics;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.core.commands.CommandSetup;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;

public class GenericGuildController {

    public GenericGuildController(GenericGuildEvent e) {
        if(e instanceof GuildJoinEvent) {
            guildJoinEvent((GuildJoinEvent)e);
        } else if(e instanceof GuildLeaveEvent) {
            guildLeaveEvent((GuildLeaveEvent)e);
        } else if(e instanceof GuildMemberJoinEvent) {
            guildMemberJoinEvent((GuildMemberJoinEvent)e);
        }
    }

    private void guildJoinEvent(GuildJoinEvent e) {
        new CommandSetup().executeAutomated(e);
        Statistics.GUILD_COUNT = Cache.JDA.getGuilds().size();

        try {
            e.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().toLowerCase().contains("general")).findFirst().ifPresent(textChannel -> {
                EmbedBuilder about = new EmbedBuilder()
                        .setAuthor(Cache.BOT.getName() + "#" + Cache.BOT.getDiscriminator(), null, Cache.BOT.getAvatarUrl())
                        .setDescription("Automatic setup was successful! Thanks for inviting me to your server, below is information about myself. Commands can be found [here](https://github.com/BasketBandit/Yuuko)! If you have any problems, suggestions, or general feedback, please join the (support server)[https://discord.gg/QcwghsA] and let yourself be known!")
                        .setThumbnail(Cache.BOT.getAvatarUrl())
                        .addField("Author", "[0x00000000#0001](https://github.com/BasketBandit/)", true)
                        .addField("Version", Configuration.VERSION, true)
                        .addField("Servers", Statistics.GUILD_COUNT + "", true)
                        .addField("Commands", Cache.COMMANDS.size() + "", true)
                        .addField("Invocation", Configuration.GLOBAL_PREFIX + ", `" + Utils.getServerPrefix(e.getGuild().getId()) + "`", true)
                        .addField("Uptime", Statistics.RUNTIME.toString(), true)
                        .addField("Ping", Statistics.PING + "", true);
                MessageHandler.sendMessage(textChannel, about.build());
            });
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "GuildJoinEvent -> Initial Message");
        }

        Utils.updateDiscordBotList();
        Utils.updateLatest("[INFO] Joined server: " + e.getGuild().getName() + " (Id: " + e.getGuild().getIdLong() + ", Users: " + e.getGuild().getMemberCache().size() + ")");
    }

    private void guildLeaveEvent(GuildLeaveEvent e) {
        new DatabaseFunctions().cleanup(e.getGuild().getId());
        Statistics.GUILD_COUNT = Cache.JDA.getGuilds().size();
        Utils.updateDiscordBotList();
        Utils.updateLatest("[INFO] Left server: " + e.getGuild().getName() + " (Id: " + e.getGuild().getIdLong() + ", Users: " + e.getGuild().getMemberCache().size() + ")");
    }

    private void guildMemberJoinEvent(GuildMemberJoinEvent e) {
        Statistics.MEMBERS_JOINED.getAndIncrement();

        if(new DatabaseFunctions().getServerSetting("welcomeMembers", e.getGuild().getId()).equals("1")) {
            e.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().toLowerCase().contains("general")).findFirst().ifPresent(textChannel -> {
                EmbedBuilder member = new EmbedBuilder().setTitle("New Member").setDescription("Welcome to **" + e.getGuild().getName() + "**, " + e.getMember().getAsMention() + "!");
                MessageHandler.sendMessage(textChannel, member.build());
            });
        }
    }

}
