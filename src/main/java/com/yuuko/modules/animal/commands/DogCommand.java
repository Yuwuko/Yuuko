package com.yuuko.modules.animal.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class DogCommand extends Command {
    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/random";

    public DogCommand() {
        super("dog", 0, -1L, Arrays.asList("-dog"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title"))
                .setImage(new RequestHandler(BASE_URL).getJsonObject().get("message").getAsString());
        MessageDispatcher.reply(context, embed.build());
    }

}
