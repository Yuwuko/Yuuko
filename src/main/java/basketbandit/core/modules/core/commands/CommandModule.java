package basketbandit.core.modules.core.commands;

import basketbandit.core.Database;
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

    public CommandModule(MessageReceivedEvent e) {
        super("module", "basketbandit.core.modules.core.ModuleCore", Permission.MANAGE_PERMISSIONS);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException ;
     */
    protected void executeCommand(MessageReceivedEvent e) {
        String[] command = e.getMessage().getContentRaw().split("\\s+", 3);
        String value = command[1].toLowerCase();
        String serverLong = e.getGuild().getIdLong()+"";

        if(new Database().toggleModule("mod" + value, serverLong)) {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setAuthor(value + " was enabled on this server!");
            e.getTextChannel().sendMessage(embed.build()).queue();
        } else {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor(value + " was disabled on this server!");
            e.getTextChannel().sendMessage(embed.build()).queue();
        }

    }

}
