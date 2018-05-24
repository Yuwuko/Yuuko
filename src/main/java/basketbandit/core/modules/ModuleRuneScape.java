package basketbandit.core.modules;

import basketbandit.core.commands.C;
import basketbandit.core.commands.CommandRuneScapeStats;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleRuneScape extends Module {

    ModuleRuneScape() {
        super("ModuleRuneScape", "modRuneScape");
    }

    public ModuleRuneScape(MessageReceivedEvent e) {
        super("ModuleRuneScape", "modRuneScape");

        if(!checkModuleSettings(e)) {
            return;
        }

        if(!executeCommand(e)) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
        }

    }

    protected boolean executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.equals(C.RUNESCAPE_STATS.getEffectiveName())) {
            new CommandRuneScapeStats(e);
            return true;
        }

        return false;
    }
}
