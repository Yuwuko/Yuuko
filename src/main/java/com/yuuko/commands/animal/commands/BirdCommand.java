package com.yuuko.commands.animal.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class BirdCommand extends Command {
    private static final String BASE_URL = "http://shibe.online/api/birds";

    public BirdCommand() {
        super("bird", Yuuko.MODULES.get("animal"), 0, -1L, Arrays.asList("-bird"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle("Random Bird")
                .setImage(new RequestHandler(BASE_URL).getJsonArray().get(0).getAsString());
        MessageDispatcher.reply(e, embed.build());
    }

}
