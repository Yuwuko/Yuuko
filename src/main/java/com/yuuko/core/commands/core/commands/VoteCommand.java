package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class VoteCommand extends Command {

    public VoteCommand() {
        super("vote", Config.MODULES.get("core"), 0, -1L, Arrays.asList("-vote"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        EmbedBuilder about = new EmbedBuilder()
                .setAuthor(Config.BOT.getName() + "#" + Config.BOT.getDiscriminator(), null, Config.BOT.getAvatarUrl())
                .setDescription(
                        "To help with growth, I am listed on some of the popular Discord bot lists, here you can find links on where to vote for me. " +
                        "Voting is an important part of this growth because with more votes comes more exposure and thus more people who will use me. " +
                        "I have chosen not to lock any of my features behind a vote wall, but ask you kindly to vote (up to every 24 hours) for me on your favourite list(s)!"
                )
                .addField("discordbots.org", "[link](https://discordbots.org/bot/420682957007880223/vote)", true)
                .addField("discordbotlist.com", "[link](https://discordbotlist.com/bots/420682957007880223/upvote)", true);
        MessageDispatcher.reply(e, about.build());
    }
}
