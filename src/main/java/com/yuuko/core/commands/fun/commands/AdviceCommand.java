package com.yuuko.core.commands.fun.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;

public class AdviceCommand extends Command {

    public AdviceCommand() {
        super("advice", Configuration.MODULES.get("fun"), 0, Arrays.asList("-advice"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        final JsonObject object = new RequestHandler("https://api.adviceslip.com/advice").getJsonObject();

        EmbedBuilder embed = new EmbedBuilder().setTitle("Advice")
                .setDescription(object.get("slip").getAsJsonObject().get("advice").getAsString());
        MessageHandler.sendMessage(e, embed.build());
    }
}