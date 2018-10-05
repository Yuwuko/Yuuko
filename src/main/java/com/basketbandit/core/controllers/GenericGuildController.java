package com.basketbandit.core.controllers;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.SystemClock;
import com.basketbandit.core.SystemInformation;
import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.core.commands.CommandSetup;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
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

        List<TextChannel> channels = e.getGuild().getTextChannels();
        User bot = e.getGuild().getMemberById(420682957007880223L).getUser();

        int users = 0;
        for(Guild guild : bot.getJDA().getGuilds()) {
            users += guild.getMemberCache().size();
        }
        SystemInformation.setUserCount(users);
        SystemInformation.incrementGuildCount(1);

        EmbedBuilder about = new EmbedBuilder()

                .setAuthor(bot.getName() + "#" + bot.getDiscriminator(), null, bot.getAvatarUrl())
                .setDescription("Automatic setup was successful! Thanks for inviting me to your server, below is information about myself. Commands can be found [here](https://github.com/BasketBandit/BasketBandit-Java)! If you have any problems, suggestions, or general feedback, please join the (support server)[https://discord.gg/QcwghsA] and let yourself be known!")
                .setThumbnail(bot.getAvatarUrl())
                .addField("Author", "[0x00000000#0001](https://github.com/BasketBandit/)", true)
                .addField("Version", Configuration.VERSION, true)
                .addField("Servers", SystemInformation.getGuildCount(), true)
                .addField("Users", SystemInformation.getUserCount(), true)
                .addField("Commands", SystemInformation.getCommandCount(), true)
                .addField("Invocation", Configuration.GLOBAL_PREFIX, true)
                .addField("Uptime", SystemClock.getRuntime(), true)
                .addField("Heartbeat", SystemInformation.getPing(), true);

        for(TextChannel c: channels) {
            if(c.getName().toLowerCase().equals("general")) {
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
        User bot = e.getGuild().getMemberById(420682957007880223L).getUser();

        new DatabaseFunctions().cleanup(e.getGuild().getId());

        int users = 0;
        for(Guild guild : bot.getJDA().getGuilds()) {
            users += guild.getMemberCache().size();
        }
        SystemInformation.setUserCount(users);
        SystemInformation.incrementGuildCount(-1);

        Utils.updateDiscordBotList();
        Utils.updateLatest("[INFO] Left server: " + e.getGuild().getName() + " (" + e.getGuild().getIdLong() + ")");
    }
}
