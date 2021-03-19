package com.yuuko.commands.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.utilities.Sanitiser;
import com.yuuko.utilities.StringFormatter;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class OsuCommand extends Command {
    private static final Api api = Yuuko.API_MANAGER.getApi("osu");
    private static final String BASE_URL = "https://osu.ppy.sh/api/get_user?k=" + api.getKey() + "&u=";

    public OsuCommand() {
        super("osu", Yuuko.MODULES.get("media"), 1, -1L, Arrays.asList("-osu <user>", "-osu <user> <mode>"), false, null, api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        String[] commandParameters = e.getParameters().split("\\s+", 2);

        int mode = 0;
        if(commandParameters.length > 1 && Sanitiser.isNumber(commandParameters[1])) {
            mode = Integer.parseInt(commandParameters[1]);
        }

        String modeString = switch(mode) {
            case 0 -> "osu!";
            case 1 -> "taiko";
            case 2 -> "catch the beat";
            case 3 -> "osu!mania";
            default -> "unknown";
        };
        mode = modeString.equals("unknown") ? 0 : mode;

        final String url = BASE_URL + Sanitiser.scrub(commandParameters[0], true) + "&m=" + mode;
        final JsonArray json = new RequestHandler(url).getJsonArray();
        if(json == null || json.size() < 1) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for `" + e.getParameters() + "` produced no results.");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        JsonObject data = json.get(0).getAsJsonObject();
        if(data.get("playcount").isJsonNull()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Total playcount and playtime for **_" + data.get("username").getAsString() + "_** has returned as null, which indicates that they haven't played **" + modeString + "** yet.");
            MessageDispatcher.reply(e, embed.build());
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
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }
}
