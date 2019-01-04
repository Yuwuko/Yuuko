package com.yuuko.core.modules.core.commands;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.core.CoreModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SetupCommand extends Command {

    public SetupCommand() {
        super("setup", CoreModule.class, 0, new String[]{"-setup"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        if(!new DatabaseFunctions().addNewServer(e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Setup").setDescription("Server setup was successful.");
            MessageHandler.sendMessage(e, embed.build());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Setup").setDescription("Server setup was unsuccessful.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

    public void executeAutomated(GuildJoinEvent e) {
        try {
            new DatabaseFunctions().addNewServer(e.getGuild().getId());
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Server setup was unsuccessful (" + e.getGuild().getId() + ") [SetupCommand] (Automated)");
        }
    }

}
