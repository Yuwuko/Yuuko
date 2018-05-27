package basketbandit.core.modules.game;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.game.commands.CommandRuneScapeStats;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleGame extends Module {

    public ModuleGame() {
        super("ModuleGame", "modGame");
    }

    public ModuleGame(MessageReceivedEvent e) {
        super("ModuleGame", "modGame");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e);
    }

    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.RUNESCAPE_STATS.getEffectiveName())) {
            new CommandRuneScapeStats(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}
