package com.yuuko.core.commands.animal.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class DogCommand extends Command {
    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/random";

    public DogCommand() {
        super("dog", Config.MODULES.get("animal"), 0, -1L, Arrays.asList("-dog"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        final JsonObject object = new RequestHandler(BASE_URL).getJsonObject();

        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Dog")
                .setImage(object.get("message").getAsString());
        MessageDispatcher.reply(e, embed.build());
    }

}
