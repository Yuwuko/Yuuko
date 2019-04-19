package com.yuuko.core.commands.world.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.world.WorldModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;

public class UrbanDictionaryCommand extends Command {

    public UrbanDictionaryCommand() {
        super("urban", WorldModule.class, 1, new String[]{"-urban <term>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        JsonObject json = new JsonBuffer("https://api.urbandictionary.com/v0/define?term=" + e.getCommand()[1].replace(" ", "%20"), "default", "default").getAsJsonObject();

        if(json.get("list").getAsJsonArray().size() < 1) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + e.getCommand()[1] + "_** produced no results.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        JsonObject data = json.get("list").getAsJsonArray().get(0).getAsJsonObject();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Urban Dictionary - " + data.get("word").getAsString())
                .setDescription(data.get("definition").getAsString().replace("[", "").replace("]", ""))
                .addField("Example", data.get("example").getAsString().replace("[", "").replace("]", ""), false)
                .addField("Thumbs Up", data.get("thumbs_up").getAsString(), true)
                .addField("Thumbs Down", data.get("thumbs_down").getAsString(), true)
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }

}
