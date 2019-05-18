package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.media.MediaModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.StringFormatter;
import com.yuuko.core.utilities.Utilities;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;

public class OsuCommand extends Command {

    public OsuCommand() {
        super("osu", MediaModule.class, 1, Arrays.asList("-osu <user>", "-osu <user> <mode>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            String[] commandParameters = e.getCommand().get(1).split("\\s+", 2);

            final int mode;
            if(commandParameters.length > 1) {
                if(Sanitiser.isNumber(commandParameters[1])) {
                    final int tmp = Integer.parseInt(commandParameters[1]);
                    mode = (tmp < 4 && tmp > -1) ? tmp : 0;
                } else {
                    mode = 0;
                }
            } else {
                mode = 0;
            }

            String modeString;
            switch(mode) {
                case 0: modeString = "osu!";
                    break;
                case 1: modeString = "taiko";
                    break;
                case 2: modeString = "catch the beat";
                    break;
                case 3: modeString = "osu!mania";
                    break;
                default: modeString = "unknown";
            }

            JsonArray json = new JsonBuffer("https://osu.ppy.sh/api/get_user?k=" + Utilities.getApiKey("osu") + "&u=" + commandParameters[0] + "&m=" + mode, "default", "default").getAsJsonArray();

            if(json == null || json.size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + e.getCommand().get(1) + "_** produced no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            JsonObject data = json.get(0).getAsJsonObject();

            if(data.get("playcount").isJsonNull()) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Total playcount and playtime for **_" + data.get("username").getAsString() + "_** has returned as null, which indicates that they haven't played **" + modeString + "** yet.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            final String worldRank = new StringFormatter.Builder().string(data.get("pp_rank").getAsString()).formatIntegers().prepend("#").encase("```").build().toString();
            final String countryRank = new StringFormatter.Builder().string(data.get("pp_country_rank").getAsString()).formatIntegers().prepend("#").encase("```").build().toString();
            final String accuracy = new StringFormatter.Builder().string(((data.get("accuracy").getAsDouble() > 0.0) ? (data.get("accuracy").getAsString().length() > 5) ? data.get("accuracy").getAsString().substring(0, 5) : data.get("accuracy").getAsString() : "0")).append("%").encase("```").build().toString();
            final String ssRanks = new StringFormatter.Builder().string(data.get("count_rank_ss").getAsString()).formatIntegers().encase("```").build().toString();
            final String sshRanks = new StringFormatter.Builder().string(data.get("count_rank_ssh").getAsString()).formatIntegers().encase("```").build().toString();
            final String sRanks = new StringFormatter.Builder().string(data.get("count_rank_s").getAsString()).formatIntegers().encase("```").build().toString();
            final String shRanks = new StringFormatter.Builder().string(data.get("count_rank_sh").getAsString()).formatIntegers().encase("```").build().toString();
            final String aRanks = new StringFormatter.Builder().string(data.get("count_rank_a").getAsString()).formatIntegers().encase("```").build().toString();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(data.get("username").getAsString() + " (" + data.get("country").getAsString() + ") | " + modeString)
                    .setThumbnail("https://vignette.wikia.nocookie.net/logopedia/images/d/d3/Osu%21Logo_%282015%29.png")
                    .setDescription("Account created **" + data.get("join_date").getAsString() + "**, amassing a total playcount of **" + data.get("playcount").getAsString() + "** over the course of **" + (data.get("total_seconds_played").getAsInt()/60)/60 + "** hours. In that time, also obtaining **" + data.get("pp_raw").getAsString() + "** of that delicious pp.")
                    .addField("World Rank", worldRank, true)
                    .addField("Country Rank", countryRank, true)
                    .addField("Accuracy", accuracy, true)
                    .addField("SS Ranks", ssRanks, true)
                    .addField("SSH Ranks", sshRanks, true)
                    .addField("S Ranks", sRanks, true)
                    .addField("SH Ranks", shRanks, true)
                    .addField("A Ranks", aRanks, true)
                    .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
