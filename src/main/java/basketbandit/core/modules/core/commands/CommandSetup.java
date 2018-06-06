package basketbandit.core.modules.core.commands;

import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetup extends Command {

    public CommandSetup() {
        super("setup", "basketbandit.core.modules.core.ModuleCore", Permission.ADMINISTRATOR);
    }

    public CommandSetup(MessageReceivedEvent e, String[] command) {
        super("setup", "basketbandit.core.modules.core.ModuleCore", Permission.ADMINISTRATOR);
        executeCommand(e, command);
    }

    public CommandSetup(GuildJoinEvent e) {
        super("setup", "basketbandit.core.modules.core.ModuleCore", Permission.ADMINISTRATOR);
        executeCommandAux(e);
    }


    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String serverId = e.getGuild().getId();

        if(!new DatabaseFunctions().addNewServer(serverId)) {
            e.getTextChannel().sendMessage("Server setup successful.").queue();
        } else {
            e.getTextChannel().sendMessage("Server setup was unsuccessful.").queue();
        }
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    private void executeCommandAux(GuildJoinEvent e) {
        String serverId = e.getGuild().getId();
        if(new DatabaseFunctions().addNewServer(serverId)) {
            System.out.println("[ERROR] Server setup was unsuccessful (" + e.getGuild().getId() + ")");
        }
    }

}
