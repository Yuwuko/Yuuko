package com.yuuko.modules.fun.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class JokeCommand extends Command {
    private static final String BASE_URL = "https://icanhazdadjoke.com/";

    public JokeCommand() {
        super("joke", Arrays.asList("-joke", "-joke <term>"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setDescription(new RequestHandler(BASE_URL).getJsonObject().get("joke").getAsString());
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        JsonObject object = new RequestHandler(BASE_URL + "/search?limit=30&term=" + context.getParameters().replace(" ", "%20")).getJsonObject();
        if(object.getAsJsonArray("results").size() < 1) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n( "no_results_title"))
                    .setDescription(context.i18n( "no_results_desc").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        final JsonArray jokes = object.getAsJsonArray("results");
        EmbedBuilder embed = new EmbedBuilder().setDescription(jokes.get(ThreadLocalRandom.current().nextInt(jokes.size())).getAsJsonObject().get("joke").getAsString());
        MessageDispatcher.reply(context, embed.build());
    }

}
