package com.yuuko.core.commands.animal.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.animal.AnimalModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;

public class CatCommand extends Command {

    public CatCommand() {
        super("cat", AnimalModule.class, 0, Arrays.asList("-cat"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        final JsonObject object = new RequestHandler("https://api.thecatapi.com/v1/images/search").getJsonArray().get(0).getAsJsonObject();

        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Cat")
                .setImage(object.get("url").getAsString());
        MessageHandler.sendMessage(e, embed.build());
    }

}
