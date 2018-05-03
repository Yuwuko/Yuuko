// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Random;

class ModuleMath {

    private MessageReceivedEvent e;

    ModuleMath(MessageReceivedEvent e) {
        this.e = e;
        String[] command = e.getMessage().getContentRaw().split("\\s+", 2);

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "roll")) {
            commandRoll(command);
        } else if (command[0].toLowerCase().equals(Configuration.PREFIX + "sum")) {
            commandSum(command);
        }
    }

    /**
     * Roll command, essentially random number generator.
     * @param command the original given command.
     */
    private void commandRoll(String[] command) {
        String[] rollString = command[1].split("d");
        int num = 0;
        int rollNum = 0;

        // I assume someone will try to roll something that isn't a number.
        if(rollString[1].matches("[0-9]+")) {
            rollNum = Integer.parseInt(rollString[1]);
        } else if(rollString[1].contains("-")) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() +", you can't roll on a negative number or anything that isn't a number").queue();
            return;
        }

        if(rollString[1].equals("00")) {
            num = (new Random().nextInt(10) + 1) * 10;
        } else {
            num = new Random().nextInt(rollNum) + 1;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED)
                .setAuthor(e.getGuild().getMemberById(e.getAuthor().getIdLong()).getEffectiveName() + " rolled a " + num + ".", null, e.getAuthor().getAvatarUrl());

        if(num != 0) {
            e.getTextChannel().sendMessage(embed.build()).queue();
        } else {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() +", something has gone wrong...").queue();
        }
    }

    /**
     * Sum command will make simple calculations.
     * @param command the original given command.
     */
    private void commandSum(String[] command) {
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

        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor(sumString);
        e.getTextChannel().sendMessage(embed.build()).queue();
    }
}
