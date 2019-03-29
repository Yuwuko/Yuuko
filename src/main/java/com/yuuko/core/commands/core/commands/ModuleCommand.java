package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.database.ModuleFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;

public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", CoreModule.class, 0, new String[]{"-module <module>"}, false, new Permission[]{Permission.MANAGE_SERVER});
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(e.hasParameters()) {
            String module = e.getCommand()[1].split("\\s+", 2)[0].toLowerCase();
            String guild = e.getGuild().getId();

            // Check if the module even exists.
            boolean valid = false;
            for(Module moduleObj : Configuration.MODULES) {
                if(moduleObj.getName().equalsIgnoreCase(module)) {
                    valid = true;
                    break;
                }
            }

            if(!valid) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("_" + module + "_ is not a valid module.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            if(ModuleFunctions.toggleModule(guild, module)) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("_" + module + "_ was enabled on this server!");
                MessageHandler.sendMessage(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("_" + module + "_ was disabled on this server!");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            ArrayList<ArrayList<String>> settings = ModuleFunctions.getModuleSettings(e.getGuild().getId());

            EmbedBuilder commandModules = new EmbedBuilder()
                    .setTitle("Below are the lists of my enabled/disabled modules!")
                    .setDescription("Each module can be toggled on or off by using the '" + Utilities.getServerPrefix(e.getGuild()) + "module <module>' command.")
                    .addField("Enabled Modules (" + settings.get(0).size() + ")", settings.get(0).toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), true)
                    .addField("Disabled Modules (" + settings.get(1).size() + ")", settings.get(1).toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), true)
                    .setTimestamp(Instant.now())
                    .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, commandModules.build());
        }
    }

}
