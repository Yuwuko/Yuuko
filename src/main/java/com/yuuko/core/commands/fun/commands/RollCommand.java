package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.Random;

public class RollCommand extends Command {

    public RollCommand() {
        super("roll", Config.MODULES.get("fun"), 1, -1L, Arrays.asList("-roll <number>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(!Sanitiser.isNumber(e.getParameters())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input.").setDescription("Input must be a non-negative numeric value.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle("Roll").setDescription("`" + e.getMember().getEffectiveName() + "` rolled a `d" + e.getParameters() + "` and got `" + new Random().nextInt(Integer.parseUnsignedInt(e.getParameters())) + 1 + "`.");
        MessageHandler.sendMessage(e, embed.build());
    }

}
