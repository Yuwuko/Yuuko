package com.yuuko.commands.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class AdviceCommand extends Command {

    public AdviceCommand() {
        super("advice", 0, -1L, Arrays.asList("-advice"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle("Advice")
                .setDescription(new RequestHandler("https://api.adviceslip.com/advice").getJsonObject().get("slip").getAsJsonObject().get("advice").getAsString());
        MessageDispatcher.reply(e, embed.build());
    }
}