package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.core.Cache;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.media.MediaModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.Utils;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class OsuCommand extends Command {

    public OsuCommand() {
        super("osu", MediaModule.class, 1, new String[]{"-osu [user]", "-osu [user] [media]"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 2);

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

            JsonArray json = new JsonBuffer("https://osu.ppy.sh/api/get_user?k=" + Utils.getApiKey("osu") + "&u=" + commandParameters[0] + "&m=" + mode, "default", "default", null, null).getAsJsonArray();

            if(json == null || json.size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + command[1] + "_** produced no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            JsonObject data = json.get(0).getAsJsonObject();

            if(data.get("playcount").isJsonNull()) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Total playcount and playtime for **_" + data.get("username").getAsString() + "_** has returned as null, which indicates that they haven't played **" + modeString + "** yet.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(data.get("username").getAsString() + " (" + data.get("country").getAsString() + ") | " + modeString)
                    .setThumbnail("https://vignette.wikia.nocookie.net/logopedia/images/d/d3/Osu%21Logo_%282015%29.png")
                    .setDescription("Account created **" + data.get("join_date").getAsString() + "**, amassing a total playcount of **" + data.get("playcount").getAsString() + "** over the course of **" + (data.get("total_seconds_played").getAsInt()/60)/60 + "** hours. In that time, also obtaining **" + data.get("pp_raw").getAsString() + "** of that delicious pp.")
                    .addField("World Rank", "#" + data.get("pp_rank").getAsString(), true)
                    .addField("Country Rank", "#" + data.get("pp_country_rank").getAsString(), true)
                    .addField("Accuracy", (data.get("accuracy").getAsDouble() > 0.0) ? (data.get("accuracy").getAsString().length() > 5) ? data.get("accuracy").getAsString().substring(0, 5) + "%" : data.get("accuracy").getAsString() : "0" + "%", true)
                    .addField("SS Ranks", data.get("count_rank_ss").getAsString(), true)
                    .addField("SSH Ranks", data.get("count_rank_ssh").getAsString(), true)
                    .addField("S Ranks", data.get("count_rank_s").getAsString(), true)
                    .addField("SH Ranks", data.get("count_rank_sh").getAsString(), true)
                    .addField("A Ranks", data.get("count_rank_a").getAsString(), true)
                    .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), Cache.BOT.getAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }
    }
}
