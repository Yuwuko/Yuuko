package com.yuuko.core.commands.animal.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class FoxCommand extends Command {

    private static final String BASE_URL = "https://randomfox.ca/floof/";

    public FoxCommand() {
        super("fox", Configuration.MODULES.get("animal"), 0, Arrays.asList("-fox"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        final JsonObject object = new RequestHandler(BASE_URL).getJsonObject();

        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Fox")
                .setImage(object.get("image").getAsString());
        MessageHandler.sendMessage(e, embed.build());
    }

}
