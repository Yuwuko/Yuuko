package com.yuuko.modules.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import com.yuuko.utilities.Sanitiser;
import com.yuuko.utilities.StringFormatter;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class OsuCommand extends Command {
    private static final Api api = Yuuko.API_MANAGER.getApi("osu");
    private static final String BASE_URL = "https://osu.ppy.sh/api/get_user?k=" + api.getKey() + "&u=";

    public OsuCommand() {
        super("osu", 1, -1L, Arrays.asList("-osu <user>", "-osu <user> <mode>"), false, null, api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        String[] commandParameters = context.getParameters().split("\\s+", 2);

        int mode = 0;
        if(commandParameters.length > 1 && Sanitiser.isNumeric(commandParameters[1])) {
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
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "no_results")).setDescription(context.i18n( "no_results_desc").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        JsonObject data = json.get(0).getAsJsonObject();
        if(data.get("playcount").isJsonNull()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "no_results")).setDescription(context.i18n( "no_results_ext").formatted(data.get("username").getAsString(), modeString));
            MessageDispatcher.reply(context, embed.build());
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
                .setDescription(context.i18n( "created").formatted(data.get("join_date").getAsString(), data.get("playcount").getAsString(), (data.get("total_seconds_played").getAsInt()/60)/60, data.get("pp_raw").getAsString()))
                .addField(context.i18n( "world_rank"), worldRank, true)
                .addField(context.i18n( "country_rank"), countryRank, true)
                .addField(context.i18n( "accuracy"), accuracy, true)
                .addField(context.i18n( "ss_ranks"), ssRanks, true)
                .addField(context.i18n( "ssh_ranks"), sshRanks, true)
                .addField(context.i18n( "s_ranks"), sRanks, true)
                .addField(context.i18n( "sh_ranks"), shRanks, true)
                .addField(context.i18n( "a_ranks"), aRanks, true)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }
}
