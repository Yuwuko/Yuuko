package com.yuuko.modules.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class AdviceCommand extends Command {

    public AdviceCommand() {
        super("advice", Arrays.asList("-advice"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n( "title"))
                .setDescription(new RequestHandler("https://api.adviceslip.com/advice").getJsonObject().get("slip").getAsJsonObject().get("advice").getAsString());
        MessageDispatcher.reply(context, embed.build());
    }
}