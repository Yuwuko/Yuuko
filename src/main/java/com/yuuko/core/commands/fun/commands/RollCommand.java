package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;
import java.util.Random;

public class RollCommand extends Command {

    public RollCommand() {
        super("roll", Configuration.MODULES.get("fun"), 1, Arrays.asList("-roll <number>", "-roll 00"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        int rollNum;
        if(Sanitiser.isNumber(e.getParameters())) {
            rollNum = Integer.parseUnsignedInt(e.getParameters());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input.").setDescription("Input must be a non-negative numeric value.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        int num;
        if(e.getParameters().equals("00")) {
            num = (new Random().nextInt(10) + 1) * 10;
        } else {
            num = new Random().nextInt(rollNum) + 1;
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle("Roll").setDescription("`" + e.getMember().getEffectiveName() + "` rolled a `d" + e.getParameters() + "` and got `" + num + "`.");
        MessageHandler.sendMessage(e, embed.build());
    }

}
