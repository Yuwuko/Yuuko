package com.basketbandit.core.modules.math.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSum extends Command {

    public CommandSum() {
        super("sum", "com.basketbandit.core.modules.math.ModuleMath", 1, new String[]{"-sum [variable] [operator] [variable]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String sumString = "";

        if(command[1].contains("+")) {
            String[] a = command[1].replace("+"," ").split("\\s+");
            Double sum = Double.parseDouble(a[0]) + Double.parseDouble(a[1]);
            sumString = a[0] + " + " + a[1] + " = " + sum;

        } else if(command[1].contains("/")) {
            String[] a = command[1].replace("/"," ").split("\\s+");
            Double sum = Double.parseDouble(a[0]) / Double.parseDouble(a[1]);
            sumString = a[0] + " / " + a[1] + " = " + sum;

        } else if(command[1].contains("*")) {
            String[] a = command[1].replace("*"," ").split("\\s+");
            Double sum = Double.parseDouble(a[0]) * Double.parseDouble(a[1]);
            sumString = a[0] + " * " + a[1] + " = " + sum;

        } else if(command[1].contains("-")) {
            String[] a = command[1].replace("-"," ").split("\\s+");
            Double sum = Double.parseDouble(a[0]) - Double.parseDouble(a[1]);
            sumString = a[0] + " - " + a[1] + " = " + sum;

        } else if(command[1].contains("^")) {
            String[] a = command[1].replace("^"," ").split("\\s+");
            Double sum = Math.pow(Double.parseDouble(a[0]),Double.parseDouble(a[1]));
            sumString = a[0] + "^" + a[1] + " = " + sum;

        } else if(command[1].contains("%")) {
            String[] a = command[1].replace("%"," ").split("\\s+");
            Double sum = (Double.parseDouble(a[1])/100) * Double.parseDouble(a[0]);
            sumString = a[0] + "% of " + a[1] + " = " + sum;

        } else {
            MessageHandler.sendMessage(e, "The parameters given are not valid.");
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle(sumString);
        MessageHandler.sendMessage(e, embed.build());
    }
}
