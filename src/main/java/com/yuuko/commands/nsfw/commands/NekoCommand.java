package com.yuuko.commands.nsfw.commands;

import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class NekoCommand extends Command {
    private static final String BASE_URL = "https://nekos.life/api/v2/img/";
    private static final List<String> ENDPOINTS = Arrays.asList("femdom", "nekoapi_v3", "tickle", "classic", "ngif", "erofeet", "meow", "erok", "poke", "les", "hololewd", "lewdk", "keta", "feetg", "nsfw_neko_gif", "eroyuri", "kiss", "8ball", "kuni", "tits", "pussy_jpg", "cum_jpg", "pussy", "lewdkemo", "lizard", "slap", "lewd", "cum", "cuddle", "spank", "smallboobs", "Random_hentai_gif", "avatar", "fox_girl", "nsfw_avatar", "hug", "gecg", "boobs", "pat", "feet", "smug", "kemonomimi", "solog", "holo", "wallpaper", "bj", "woof", "yuri", "trap", "anal", "baka", "blowjob", "holoero", "feed", "neko", "gasm", "hentai", "futanari", "ero", "solo", "waifu", "pwankg", "eron", "erokemo");

    public NekoCommand() {
        super("neko", Yuuko.MODULES.get("nsfw"), 0, -1L, Arrays.asList("-neko", "-neko <type>"), true, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        final String url = BASE_URL + ((e.hasParameters() && ENDPOINTS.contains(e.getParameters().toLowerCase())) ? e.getParameters().toLowerCase() : "lewd");

        final JsonObject json = new RequestHandler(url).getJsonObject();
        if(json != null && !json.has("msg")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Neko: " + json.get("url").getAsString())
                    .setImage(json.get("url").getAsString())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription(Arrays.toString(ENDPOINTS.toArray()));
        MessageDispatcher.reply(e, embed.build());
    }

}
