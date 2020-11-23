package com.yuuko.core.commands.animal.commands;

import com.google.gson.JsonArray;
import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class BirdCommand extends Command {
    private static final String BASE_URL = "http://shibe.online/api/birds";

    public BirdCommand() {
        super("bird", Config.MODULES.get("animal"), 0, -1L, Arrays.asList("-bird"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        final JsonArray object = new RequestHandler(BASE_URL).getJsonArray();

        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Bird")
                .setImage(object.get(0).getAsString());
        MessageHandler.reply(e, embed.build());
    }

}
