package com.yuuko.commands.fun.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class JokeCommand extends Command {
    private static final String BASE_URL = "https://icanhazdadjoke.com/";

    public JokeCommand() {
        super("joke", Yuuko.MODULES.get("fun"), 0, 1L, Arrays.asList("-joke", "-joke <term>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setDescription(new RequestHandler(BASE_URL).getJsonObject().get("joke").getAsString());
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        JsonObject object = new RequestHandler(BASE_URL + "/search?limit=30&term=" + e.getParameters().replace(" ", "%20")).getJsonObject();
        if(object.getAsJsonArray("results").size() < 1) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("No Results")
                    .setDescription("Search for `" + e.getParameters() + "` produced no results.");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        final JsonArray jokes = object.getAsJsonArray("results");
        EmbedBuilder embed = new EmbedBuilder().setDescription(jokes.get(ThreadLocalRandom.current().nextInt(jokes.size())).getAsJsonObject().get("joke").getAsString());
        MessageDispatcher.reply(e, embed.build());
    }

}
