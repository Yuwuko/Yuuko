package com.yuuko.core.commands.animal.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class CatCommand extends Command {
    private static final String BASE_URL = "https://api.thecatapi.com/v1/images/search";

    public CatCommand() {
        super("cat", Config.MODULES.get("animal"), 0, -1L, Arrays.asList("-cat"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Cat")
                .setImage(new RequestHandler(BASE_URL).getJsonArray().get(0).getAsJsonObject().get("url").getAsString());
        MessageDispatcher.reply(e, embed.build());
    }

}
