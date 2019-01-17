package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", CoreModule.class, 1, new String[]{"-module [module]"}, new Permission[]{Permission.MANAGE_SERVER});
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String moduleName = command[1].split("\\s+",2)[0].toLowerCase();
        String server = e.getGuild().getId();

        // Check if the module even exists.
        boolean hit = false;
        for(Module module: Cache.MODULES) {
            if(module.getName().equalsIgnoreCase(moduleName)) {
                hit = true;
                break;
            }
        }

        if(!hit) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("_" + moduleName + "_ is not a valid module.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(new DatabaseFunctions().toggleModule("module" + moduleName, server)) {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("_" + moduleName + "_ was enabled on this server!");
            MessageHandler.sendMessage(e, embed.build());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("_" + moduleName + "_ was disabled on this server!");
            MessageHandler.sendMessage(e, embed.build());
        }

    }

}
