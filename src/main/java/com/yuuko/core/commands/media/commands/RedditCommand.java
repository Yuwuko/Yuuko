package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.media.MediaModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RedditCommand extends Command {

    public RedditCommand() {
        super("reddit", MediaModule.class, 1, new String[]{"-reddit [subreddit]"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        JsonObject json = new JsonBuffer("https://www.reddit.com/r/" + command[1] + "/new.json?sort=new&limit=1", "default", "default", null, null).getAsJsonObject();

        if(json == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + command[1] + "_** produced no results.");
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
