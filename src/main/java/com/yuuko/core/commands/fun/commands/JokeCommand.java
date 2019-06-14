package com.yuuko.core.commands.fun.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.fun.FunModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class JokeCommand extends Command {
    private static final String BASE_URL = "https://icanhazdadjoke.com/";

    public JokeCommand() {
        super("joke", FunModule.class, 0, Arrays.asList("-joke", "-joke <term>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        String joke;

        if(!e.hasParameters()) {
            joke = new RequestHandler(BASE_URL).getJsonObject().get("joke").getAsString();
        } else {
            JsonObject object = new RequestHandler(BASE_URL + "/search?limit=30&term=" + e.getCommand().get(1).replace(" ", "%20")).getJsonObject();

            if(object.getAsJsonArray("results").size() < 1) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("No Results")
                        .setDescription("Search for `" + e.getCommand().get(1) + "` produced no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            JsonArray array = object.getAsJsonArray("results");
            int index = ThreadLocalRandom.current().nextInt(array.size());
            joke = array.get(index).getAsJsonObject().get("joke").getAsString();
        }

        EmbedBuilder embed = new EmbedBuilder().setDescription(joke);
        MessageHandler.sendMessage(e, embed.build());
    }

}
