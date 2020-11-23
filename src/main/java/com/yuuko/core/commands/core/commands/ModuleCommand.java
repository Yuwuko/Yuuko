package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.ModuleFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", Config.MODULES.get("core"), 0, -1L, Arrays.asList("-module <module>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(e.hasParameters()) {
            String module = e.getParameters().split("\\s+", 2)[0].toLowerCase();
            String guild = e.getGuild().getId();

            if(!Config.MODULES.containsKey(module)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("_" + module + "_ is not a valid module.");
                MessageHandler.reply(e, embed.build());
                return;
            }

            // Prevents locked modules from being disabled (would throw exception anyway)
            if(Config.LOCKED_MODULES.contains(module)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Module")
                        .setDescription("The `" + Config.LOCKED_MODULES.toString() + "` modules cannot be toggled.");
                MessageHandler.reply(e, embed.build());
                return;
            }

            if(ModuleFunctions.toggleModule(guild, module)) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("_" + module + "_ was enabled on this server!");
                MessageHandler.reply(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("_" + module + "_ was disabled on this server!");
                MessageHandler.reply(e, embed.build());
            }
        } else {
            ArrayList<ArrayList<String>> settings = ModuleFunctions.getModuleSettings(e.getGuild().getId());

            EmbedBuilder commandModules = new EmbedBuilder()
                    .setTitle("Below are the lists of my enabled/disabled modules!")
                    .setDescription("Each module can be toggled on or off by using the '" + Utilities.getServerPrefix(e.getGuild()) + "module <module>' command.")
                    .addField("Enabled Modules (" + settings.get(0).size() + ")", settings.get(0).toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), true)
                    .addField("Disabled Modules (" + settings.get(1).size() + ")", settings.get(1).toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), true)
                    .setTimestamp(Instant.now())
                    .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.reply(e, commandModules.build());
        }
    }

}
