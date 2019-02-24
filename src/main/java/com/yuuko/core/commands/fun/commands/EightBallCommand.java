package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.fun.FunModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.Random;

public class EightBallCommand extends Command {

    private static final String[] responses = new String[]{
            "It is certain.",
            "It is decidedly so.",
            "Without a doubt.",
            "Yes - definitely.",
            "You may rely on it.",
            "As I see it, yes.",
            "Most likely.",
            "Outlook good.",
            "Yes.",
            "Signs point to yes.",
            "Reply hazy, try again.",
            "Ask again later.",
            "Better not tell you now.",
            "Cannot predict now.",
            "Concentrate and ask again.",
            "Don't count on it.",
            "My reply is no.",
            "My sources say no.",
            "Outlook not so good.",
            "Very doubtful."
    };

    public EightBallCommand() {
        super("8ball", FunModule.class, 1, new String[]{"-8ball [question]"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        String question = command[1];
        question += (question.lastIndexOf("?") == question.length()-1) ? "" : "?";

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("8ball, " + question)
                .setDescription(responses[new Random().nextInt(responses.length -1)])
                .setTimestamp(Instant.now())
                .setFooter(Configuration.STANDARD_STRINGS[2] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
