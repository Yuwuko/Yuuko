package com.yuuko.modules.core.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class VoteCommand extends Command {

    public VoteCommand() {
        super("vote", 0, -1L, Arrays.asList("-vote"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder about = new EmbedBuilder()
                .setAuthor(Yuuko.BOT.getName() + "#" + Yuuko.BOT.getDiscriminator(), null, Yuuko.BOT.getAvatarUrl())
                .setDescription(I18n.getText(e, "desc"))
                .addField("discordbots.org", "[link](https://discordbots.org/bot/420682957007880223/vote)", true)
                .addField("discordbotlist.com", "[link](https://discordbotlist.com/bots/420682957007880223/upvote)", true);
        MessageDispatcher.reply(e, about.build());
    }
}
