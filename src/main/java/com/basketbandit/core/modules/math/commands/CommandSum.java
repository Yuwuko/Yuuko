package com.basketbandit.core.modules.math.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandSum extends Command {

    public CommandSum() {
        super("sum", "com.basketbandit.core.modules.math.ModuleMath", new String[]{"-sum [variable] [operator] [variable]"}, null);
    }

    public CommandSum(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }



    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String sumString;

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
            throw new IllegalArgumentException();
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor(sumString);
        Utils.sendMessage(e, embed.build());
    }

    // TODO: Remake the sum class, try to parse full equations using BODMAS.
}
