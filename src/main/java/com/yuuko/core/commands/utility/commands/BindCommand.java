package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.utility.UtilityModule;
import com.yuuko.core.database.ModuleBindFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.TextUtility;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", UtilityModule.class, 0, new String[]{"-bind [module]", "-bind [module] #channel..."}, false, new Permission[]{Permission.MANAGE_SERVER});
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        if(command.length > 1) {
            String[] commandParameters = command[1].split("\\s+", 2);
            String selectedModule = "";

            boolean present = false;
            for(Module module : Configuration.MODULES) {
                if(module.getName().equalsIgnoreCase(commandParameters[0])) {
                    selectedModule = module.getName();
                    present = true;
                    break;
                }
            }

            if(!present) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("**" + commandParameters[0] + "** isn't a valid module. A list of valid commands can be found by using the **" + Utils.getServerPrefix(e.getGuild().getId()) + "commands** command.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            if(commandParameters.length > 1) {
                List<TextChannel> channels = e.getMessage().getMentionedChannels();

                if(channels.size() < 1) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Expected at least **1** \\#tagged channel to bind, found **0**.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                StringBuilder boundChannels = new StringBuilder();
                for(TextChannel channel : channels) {
                    ModuleBindFunctions.toggleBind(e.getGuild().getId(), channel.getId(), selectedModule);
                    boundChannels.append(channel.getName()).append(", ");
                }
                TextUtility.removeLastOccurrence(boundChannels, ", ");

                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully toggled **" + selectedModule + "** on **" + boundChannels.toString() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            } else {
                final int res = ModuleBindFunctions.toggleBind(e.getGuild().getId(), e.getTextChannel().getId(), selectedModule);

                if(res == 0) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully bound **" + selectedModule + "** to **" + e.getTextChannel().getName() + "**.");
                    MessageHandler.sendMessage(e, embed.build());
                } else if(res == 1) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully removed binding of **" + selectedModule + "** from **" + e.getTextChannel().getName() + "**.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
        } else {
            try {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Bound Modules")
                        .setDescription(ModuleBindFunctions.getGuildBinds(e.getGuild(), "\n"));
                MessageHandler.sendMessage(e, embed.build());
            } catch(Exception ex) {
                //
            }
        }

    }

}
