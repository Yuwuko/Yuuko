package com.yuuko.core.commands.animal.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class FoxCommand extends Command {
    private static final String BASE_URL = "https://randomfox.ca/floof/";

    public FoxCommand() {
        super("fox", Yuuko.MODULES.get("animal"), 0, -1L, Arrays.asList("-fox"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Fox")
                .setImage(new RequestHandler(BASE_URL).getJsonObject().get("image").getAsString());
        MessageDispatcher.reply(e, embed.build());
    }

}
