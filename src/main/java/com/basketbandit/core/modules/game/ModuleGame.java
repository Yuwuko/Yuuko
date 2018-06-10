package com.basketbandit.core.modules.game;

import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.game.commands.CommandRuneScapeStats;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleGame extends Module {

    public ModuleGame() {
        super("ModuleGame", "moduleGame");
    }

    public ModuleGame(MessageReceivedEvent e, String[] command) {
        super("ModuleGame", "moduleGame");

        if(checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.RUNESCAPE_STATS.getCommandName())) {
            new CommandRuneScapeStats(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}
