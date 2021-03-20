package com.yuuko.commands.animal.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class DogCommand extends Command {
    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/random";

    public DogCommand() {
        super("dog", 0, -1L, Arrays.asList("-dog"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Dog")
                .setImage(new RequestHandler(BASE_URL).getJsonObject().get("message").getAsString());
        MessageDispatcher.reply(e, embed.build());
    }

}
