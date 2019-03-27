package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.media.MediaModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;

public class RedditCommand extends Command {

    public RedditCommand() {
        super("reddit", MediaModule.class, 1, new String[]{"-reddit <subreddit>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        JsonObject json = new JsonBuffer("https://www.reddit.com/r/" + e.getCommandParameter() + "/new.json?sort=new&limit=1", "default", "default").getAsJsonObject();

        if(json == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + e.getCommandParameter() + "_** produced no results.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Reddit:")
                .setThumbnail("")
                .setDescription("")
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
