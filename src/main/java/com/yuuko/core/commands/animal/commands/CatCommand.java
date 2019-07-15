package com.yuuko.core.commands.animal.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class CatCommand extends Command {

    private static final String BASE_URL = "https://api.thecatapi.com/v1/images/search";

    public CatCommand() {
        super("cat", Configuration.MODULES.get("animal"), 0, Arrays.asList("-cat"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        final JsonObject object = new RequestHandler(BASE_URL).getJsonArray().get(0).getAsJsonObject();

        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Cat")
                .setImage(object.get("url").getAsString());
        MessageHandler.sendMessage(e, embed.build());
    }

}
