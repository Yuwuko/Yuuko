package com.basketbandit.core.modules.media;

import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.media.commands.CommandKitsu;
import com.basketbandit.core.modules.media.commands.CommandOsuStats;
import com.basketbandit.core.modules.media.commands.CommandRuneScapeStats;
import com.basketbandit.core.modules.media.commands.CommandWorldOfWarcraftStats;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleMedia extends Module {

    public ModuleMedia() {
        super("ModuleMedia", "moduleMedia");
    }

    public ModuleMedia(MessageReceivedEvent e, String[] command) {
        super("ModuleMedia", "moduleMedia");

        if(checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.RUNESCAPE.getCommandName())) {
            new CommandRuneScapeStats(e, command);
            return;
        }

        if(command[0].equals(C.WOW.getCommandName())) {
            new CommandWorldOfWarcraftStats(e, command);
            return;
        }

        if(command[0].equals(C.OSU.getCommandName())) {
            new CommandOsuStats(e, command);
            return;
        }

        if(command[0].equals(C.KITSU.getCommandName())) {
            new CommandKitsu(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}
