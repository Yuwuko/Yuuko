package basketbandit.core.modules.core.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.NoSuchElementException;

public class CommandModule extends Command {

    public CommandModule() {
        super("module", "basketbandit.core.modules.core.ModuleCore", Permission.MANAGE_PERMISSIONS);
    }

    public CommandModule(MessageReceivedEvent e, String[] command) {
        super("module", "basketbandit.core.modules.core.ModuleCore", Permission.MANAGE_PERMISSIONS);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException ;
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String value = command[1].toLowerCase();
        String serverLong = e.getGuild().getId();

        if(new DatabaseFunctions().toggleModule("module" + value, serverLong)) {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setAuthor(value + " was enabled on this server!");
            e.getTextChannel().sendMessage(embed.build()).queue();
        } else {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor(value + " was disabled on this server!");
            e.getTextChannel().sendMessage(embed.build()).queue();
        }

    }

}
