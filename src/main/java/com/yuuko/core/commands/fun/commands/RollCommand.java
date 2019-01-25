package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.fun.FunModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class RollCommand extends Command {

    public RollCommand() {
        super("roll", FunModule.class, 1, new String[]{"-roll [number]", "-roll 00"}, false, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        int rollNum;
        if(Sanitiser.isNumber(command[1])) {
            rollNum = Integer.parseUnsignedInt(command[1]);
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input.").setDescription("Input must be a non-negative numeric value.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        int num;
        if(command[1].equals("00")) {
            num = (new Random().nextInt(10) + 1) * 10;
        } else {
            num = new Random().nextInt(rollNum) + 1;
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle("Roll").setDescription("**" + e.getMember().getEffectiveName() + "** rolled a **d" + command[1] + "** and got **" + num + "**.");
        MessageHandler.sendMessage(e, embed.build());
    }

}
