package basketbandit.core.modules.math.commands;

import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandSum extends Command {

    public CommandSum() {
        super("sum", "basketbandit.core.modules.math.ModuleMath", null);
    }

    public CommandSum(MessageReceivedEvent e, String[] command) {
        super("sum", "basketbandit.core.modules.math.ModuleMath", null);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws IllegalArgumentException;
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) throws IllegalArgumentException {
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
    }

    // TODO: Remake the sum class, try to parse full equations using BODMAS.
}
