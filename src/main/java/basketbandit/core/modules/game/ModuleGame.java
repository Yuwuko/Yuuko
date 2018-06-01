package basketbandit.core.modules.game;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.game.commands.CommandRuneScapeStats;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleGame extends Module {

    public ModuleGame() {
        super("ModuleGame", "moduleGame");
    }

    public ModuleGame(MessageReceivedEvent e, String prefix) {
        super("ModuleGame", "moduleGame");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, prefix);
    }

    protected void executeCommand(MessageReceivedEvent e, String prefix) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0].substring(prefix.length());

        if(command.equals(C.RUNESCAPE_STATS.getCommandName())) {
            new CommandRuneScapeStats(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
