package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class ModulesCommand extends Command {

    public ModulesCommand() {
        super("commands", CoreModule.class, 0, new String[]{"-commands"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            ArrayList<ArrayList<String>> settings = DatabaseFunctions.getModuleSettings(e.getGuild().getId());

            EmbedBuilder commandModules = new EmbedBuilder()
                    .setTitle("Below are the list of bot commands!")
                    .setDescription("Each module can be toggled on or off by using the '" + Utils.getServerPrefix(e.getGuild().getId()) + "module <module>' command.")
                    .addField("Enabled Modules", settings.get(0).toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), true)
                    .addField("Disabled Modules", settings.get(1).toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), true)
                    .setFooter(Cache.STANDARD_STRINGS[0], e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, commandModules.build());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }

    }

}
