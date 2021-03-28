package com.yuuko.modules.animal.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class BirdCommand extends Command {
    private static final String BASE_URL = "http://shibe.online/api/birds";

    public BirdCommand() {
        super("bird", 0, -1L, Arrays.asList("-bird"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "embed_title"))
                .setImage(new RequestHandler(BASE_URL).getJsonArray().get(0).getAsString());
        MessageDispatcher.reply(e, embed.build());
    }

}
