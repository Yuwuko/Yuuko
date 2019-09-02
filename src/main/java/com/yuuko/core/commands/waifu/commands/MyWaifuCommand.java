package com.yuuko.core.commands.waifu.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.WaifuFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.HashMap;

public class MyWaifuCommand extends Command {

    public MyWaifuCommand() {
        super("mywaifu", Configuration.MODULES.get("waifu"), 0, Arrays.asList("-mywaifu"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        HashMap<String, String> waifu = WaifuFunctions.getWaifu(e.getAuthor().getId());
        EmbedBuilder waifuInfo = new EmbedBuilder()
                .setTitle(e.getAuthor().getAsMention() + "'s Waifu")
                .setThumbnail(waifu.get("image"))
                .addField("Name", waifu.get("name"), true)
                .addField("Age", waifu.get("age"), true)
                .addField("Height", waifu.get("height"), true);
        MessageHandler.sendMessage(e, waifuInfo.build());
    }
}
