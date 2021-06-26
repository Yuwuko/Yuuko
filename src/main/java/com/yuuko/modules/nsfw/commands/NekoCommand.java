package com.yuuko.modules.nsfw.commands;

import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class NekoCommand extends Command {
    private static final String BASE_URL = "https://nekos.life/api/v2/img/";
    private static final List<String> ENDPOINTS = Arrays.asList("femdom", "nekoapi_v3", "tickle", "classic", "ngif", "erofeet", "meow", "erok", "poke", "les", "hololewd", "lewdk", "keta", "feetg", "nsfw_neko_gif", "eroyuri", "kiss", "8ball", "kuni", "tits", "pussy_jpg", "cum_jpg", "pussy", "lewdkemo", "lizard", "slap", "lewd", "cum", "cuddle", "spank", "smallboobs", "Random_hentai_gif", "avatar", "fox_girl", "nsfw_avatar", "hug", "gecg", "boobs", "pat", "feet", "smug", "kemonomimi", "solog", "holo", "wallpaper", "bj", "woof", "yuri", "trap", "anal", "baka", "blowjob", "holoero", "feed", "neko", "gasm", "hentai", "futanari", "ero", "solo", "waifu", "pwankg", "eron", "erokemo");

    public NekoCommand() {
        super("neko", Arrays.asList("-neko", "-neko <type>"), true);
    }

    @Override
    public void onCommand(MessageEvent context) {
        final String url = BASE_URL + ((context.hasParameters() && ENDPOINTS.contains(context.getParameters().toLowerCase())) ? context.getParameters().toLowerCase() : "lewd");

        final JsonObject json = new RequestHandler(url).getJsonObject();
        if(json != null && !json.has("msg")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Neko: " + json.get("url").getAsString())
                    .setImage(json.get("url").getAsString())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n("bad_input")).setDescription(Arrays.toString(ENDPOINTS.toArray()));
        MessageDispatcher.reply(context, embed.build());
    }

}
