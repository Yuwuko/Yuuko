package com.yuuko.modules.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
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
    public void onCommand(MessageEvent e) throws Exception {
        String[] commandParameters = e.getParameters().split("\\s+", 2);

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
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "no_results")).setDescription(I18n.getText(e, "no_results_desc").formatted(e.getParameters()));
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        JsonObject data = json.get(0).getAsJsonObject();
        if(data.get("playcount").isJsonNull()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "no_results")).setDescription(I18n.getText(e, "no_results_ext").formatted(data.get("username").getAsString(), modeString));
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
                .setDescription(I18n.getText(e, "created").formatted(data.get("join_date").getAsString(), data.get("playcount").getAsString(), (data.get("total_seconds_played").getAsInt()/60)/60, data.get("pp_raw").getAsString()))
                .addField(I18n.getText(e, "world_rank"), worldRank, true)
                .addField(I18n.getText(e, "country_rank"), countryRank, true)
                .addField(I18n.getText(e, "accuracy"), accuracy, true)
                .addField(I18n.getText(e, "ss_ranks"), ssRanks, true)
                .addField(I18n.getText(e, "ssh_ranks"), sshRanks, true)
                .addField(I18n.getText(e, "s_ranks"), sRanks, true)
                .addField(I18n.getText(e, "sh_ranks"), shRanks, true)
                .addField(I18n.getText(e, "a_ranks"), aRanks, true)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }
}
