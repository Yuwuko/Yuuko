package com.yuuko.modules.animal.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class FoxCommand extends Command {
    private static final String BASE_URL = "https://randomfox.ca/floof/";

    public FoxCommand() {
        super("fox", 0, -1L, Arrays.asList("-fox"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title"))
                .setImage(new RequestHandler(BASE_URL).getJsonObject().get("image").getAsString());
        MessageDispatcher.reply(context, embed.build());
    }

}
