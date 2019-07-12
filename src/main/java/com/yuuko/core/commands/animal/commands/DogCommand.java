package com.yuuko.core.commands.animal.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.animal.AnimalModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
public class DogCommand extends Command {

    public DogCommand() {
        super("dog", AnimalModule.class, 0, Arrays.asList("-dog"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        final JsonObject object = new RequestHandler("https://dog.ceo/api/breeds/image/random").getJsonObject();

        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Dog")
                .setImage(object.get("message").getAsString());
        MessageHandler.sendMessage(e, embed.build());
    }

}
