package com.yuuko.modules.animal.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class BirdCommand extends Command {
    private static final String BASE_URL = "http://shibe.online/api/birds";

    public BirdCommand() {
        super("bird", Arrays.asList("-bird"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title"))
                .setImage(new RequestHandler(BASE_URL).getJsonArray().get(0).getAsString());
        MessageDispatcher.reply(context, embed.build());
    }

}
