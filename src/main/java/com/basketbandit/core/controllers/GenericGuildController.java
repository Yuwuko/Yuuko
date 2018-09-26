package com.basketbandit.core.controllers;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.TimeKeeper;
import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.core.commands.CommandSetup;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.awt.*;
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
        List<TextChannel> channels = e.getGuild().getTextChannels();
        User bot = e.getGuild().getMemberById(420682957007880223L).getUser();

        int users = 0;
        for(Guild guild : bot.getJDA().getGuilds()) {
            users += guild.getMemberCache().size();
        }

        EmbedBuilder about = new EmbedBuilder()
                .setColor(Color.WHITE)
                .setAuthor(bot.getName() + "#" + bot.getDiscriminator(), null, bot.getAvatarUrl())
                .setDescription("Thanks for inviting me to your server! Below is a little bit of information about myself, and you can access a list of my modules [here](https://github.com/BasketBandit/BasketBandit-Java)! If you have any problems, suggestions, or general feedback, please join the (support server)[https://discord.gg/QcwghsA] and let yourself be known!")
                .setThumbnail(bot.getAvatarUrl())
                .addField("Author", "[0x00000000#0001](https://github.com/BasketBandit/)", true)
                .addField("Version", Configuration.VERSION, true)
                .addField("Servers", bot.getJDA().getGuildCache().size()+"", true)
                .addField("Users", users+"", true)
                .addField("Commands", Utils.commandCount, true)
                .addField("Invocation", Configuration.GLOBAL_PREFIX, true)
                .addField("Uptime", TimeKeeper.runtime, true)
                .addField("Ping", bot.getJDA().getPing()+"", true);

        for(TextChannel c: channels) {
            if(c.getName().toLowerCase().equals("general")) {
                try {
                    c.sendMessage(about.build()).queue();
                    break;
                } catch(PermissionException ex) {
                    System.out.printf("[INFO] Server disallowed message to be sent to general - %s (%s) \n", e.getGuild().getName(), e.getGuild().getId());
                }
            }
        }

        Utils.updateDiscordBotList();

        new CommandSetup(e);
        System.out.printf("[INFO] Joined new server: %s (Id: %s, Users: %s) \n", e.getGuild().getName(), e.getGuild().getIdLong(), e.getGuild().getMemberCache().size());
    }

    private void guildLeaveEvent(GuildLeaveEvent e) {
        new DatabaseFunctions().cleanup(e.getGuild().getId());
        Utils.updateDiscordBotList();
        System.out.printf("[INFO] Left server: %s (%s) \n", e.getGuild().getName(), e.getGuild().getIdLong());
    }
}
