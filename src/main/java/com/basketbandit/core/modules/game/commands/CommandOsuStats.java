package com.basketbandit.core.modules.game.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.game.osu.User;
import com.basketbandit.core.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandOsuStats extends Command {

    public CommandOsuStats() {
        super("osu", "com.basketbandit.core.modules.game.ModuleGame", new String[]{"-osu [user]", "-osu [user] [game]"}, null);
    }

    public CommandOsuStats(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 2);
            String username = commandParameters[0];
            int mode;

            if(commandParameters.length > 1) {
                mode = Integer.parseInt(commandParameters[1]);
            } else {
                mode = 0;
            }

            String modeString;
            switch(mode) {
                case 0: modeString = "Osu!";
                    break;
                case 1: modeString = "Taiko";
                    break;
                case 2: modeString = "Catch the Beat";
                    break;
                case 3: modeString = "Osu!mania";
                    break;
                default: modeString = "Unknown";
            }

            // Buffers JSON from the given URL and the uses ObjectMapper to turn it into usable Java objects.
            String json = Utils.bufferJson("https://osu.ppy.sh/api/get_user&k=" + Configuration.OSU_API + "&u=" + username + "&m=" + mode);
            User user = new ObjectMapper().readValue(json, new TypeReference<User>(){});

            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle(modeString + " stats for " + user.getUsername())
                    .addField("Accuracy", user.getAccuracy() + "%", true)
                    .addField("Time Played (hours)", ((Integer.parseInt(user.getTotalSecondsPlayed())/60)/60)+"", true)
                    .addField("Country", user.getCountry(), true)
                    .addField("Level", user.getLevel(), true)
                    .addField("Rank", "#"+ user.getPpRank() + "("+ user.getPpRaw() +"pp)", true)
                    .addField("Playcount", user.getPlaycount(), true)
                    .addField("SS+ Ranks", user.getCountRankSsh(), true)
                    .addField("SS Ranks", user.getCountRankSs(), true)
                    .addField("S+ Ranks", user.getCountRankSh(), true)
                    .addField("A Ranks", user.getCountRankA()+"", true)
                    .setFooter("Version: " + Configuration.VERSION + ", Data provided by osu.ppy.sh" , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            Utils.sendMessage(e, embed.build());

        } catch(Exception ex) {
            Utils.sendMessage(e, "There was an issue processing the request for command: " + e.getMessage().getContentDisplay());
        }
    }
}
