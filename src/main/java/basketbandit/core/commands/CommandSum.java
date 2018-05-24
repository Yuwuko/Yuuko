package basketbandit.core.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandSum extends Command {

    CommandSum() {
        super("sum", "basketbandit.core.modules.ModuleMath", null);
    }

    public CommandSum(MessageReceivedEvent e) {
        super("sum", "basketbandit.core.modules.ModuleMath", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws IllegalArgumentException;
     */
    protected boolean executeCommand(MessageReceivedEvent e) throws IllegalArgumentException {
        String[] command = e.getMessage().getContentRaw().split("\\s+", 3);
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
        e.getTextChannel().sendMessage(embed.build()).queue();
        return true;
    }

    // TODO: Remake the sum class, try to parse full equations using BODMAS.
}
