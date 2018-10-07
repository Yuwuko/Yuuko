package com.basketbandit.core.modules.core.commands;

import com.basketbandit.core.Cache;
import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandModule extends Command {

    public CommandModule() {
        super("module", "com.basketbandit.core.modules.core.ModuleCore", 1, new String[]{"-module [module]"}, Permission.MANAGE_SERVER);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String moduleName = command[1].toLowerCase();
        String serverLong = e.getGuild().getId();

        // Check if the module even exists.
        boolean hit = false;
        for(Module module: Cache.MODULES) {
            if(module.getModuleName().substring(6).toLowerCase().equals(command[1])) {
                hit = true;
                break;
            }
        }

        if(!hit) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("_" + moduleName + "_ is not a valid module.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(new DatabaseFunctions().toggleModule("module" + moduleName, serverLong)) {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("_" + moduleName + "_ was enabled on this server!");
            MessageHandler.sendMessage(e, embed.build());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("_" + moduleName + "_ was disabled on this server!");
            MessageHandler.sendMessage(e, embed.build());
        }

    }

}
