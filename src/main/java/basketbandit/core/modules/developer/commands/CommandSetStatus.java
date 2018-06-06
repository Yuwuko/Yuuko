package basketbandit.core.modules.developer.commands;

import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetStatus extends Command {

    public CommandSetStatus() {
        super("setstatus", "basketbandit.core.modules.developer.ModuleDeveloper", null);
    }

    public CommandSetStatus(MessageReceivedEvent e, String[] command) {
        super("setstatus", "basketbandit.core.modules.developer.ModuleDeveloper", null);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 3);

        switch(commandParameters[0]) {
            case "playing":
                e.getJDA().getPresence().setGame(Game.of(Game.GameType.DEFAULT, commandParameters[1]));
                break;
            case "listening":
                e.getJDA().getPresence().setGame(Game.of(Game.GameType.LISTENING, commandParameters[1]));
                break;
            case "streaming":
                e.getJDA().getPresence().setGame(Game.of(Game.GameType.STREAMING, commandParameters[1]));
                break;
            case "watching":
                e.getJDA().getPresence().setGame(Game.of(Game.GameType.WATCHING, commandParameters[1]));
                break;
        }

    }

}