package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VoteCommand extends Command {

    public VoteCommand() {
        super("vote", CoreModule.class, 0, new String[]{"-vote"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        EmbedBuilder about = new EmbedBuilder()
                .setAuthor(Cache.BOT.getName() + "#" + Cache.BOT.getDiscriminator(), null, Cache.BOT.getAvatarUrl())
                .setDescription(
                        "To help with growth, I am listed on some of the popular Discord bot lists, here you can find links on where to vote for me. " +
                        "Voting is an important part of this growth because with more votes comes more exposure and thus more people who will use me. " +
                        "I have chosen not to lock any of my features behind a vote wall, but ask you kindly to vote (up to every 24 hours) for me on your favourite list(s)!"
                )
                .addField("discordbots.org", "[link](https://discordbots.org/bot/420682957007880223/vote)", true)
                .addField("divinediscordbots.com", "[link](https://divinediscordbots.com/bots/420682957007880223/vote)", true)
                .addField("discordbotlist.com", "[link](https://discordbotlist.com/bots/420682957007880223/upvote)", true);
        MessageHandler.sendMessage(e, about.build());
    }
}
