package com.yuuko.core.modules.utility.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.utility.UtilityModule;
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
        super("bind", UtilityModule.class, 1, new String[]{"-bind [module]", "-bind [module] [channel]"}, new Permission[]{Permission.ADMINISTRATOR});
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 2);
        String selectedModule = "";

        boolean present = false;
        for(Module module : Cache.MODULES) {
            if(module.getName().equalsIgnoreCase(commandParameters[0])) {
                selectedModule = module.getName();
                present = true;
                break;
            }
        }

        if(!present) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("**" + commandParameters[0] + "** isn't a valid module. A list of valid modules can be found by using the '" + Utils.getServerPrefix(e.getGuild().getId()) + "modules' command.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(commandParameters.length > 1) {
            List<TextChannel> channels = e.getMessage().getMentionedChannels();

            if(channels.size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("I expected at least **1** \\#tagged channel to bind, but found **0**");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            StringBuilder boundChannels = new StringBuilder();
            for(TextChannel channel: channels) {
                new DatabaseFunctions().toggleBinding(selectedModule, channel.getId(), e.getGuild().getId());
                boundChannels.append(channel.getName()).append(", ");
            }
            TextUtility.removeLastOccurrence(boundChannels, ", ");

            EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully toggled **" + selectedModule + "** on **" + boundChannels.toString() + "**.");
            MessageHandler.sendMessage(e, embed.build());
        } else {
            String channelId = e.getTextChannel().getId();
            selectedModule = commandParameters[0];

            final int res = new DatabaseFunctions().toggleBinding(selectedModule, channelId, e.getGuild().getId());

            if(res == 0) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully bound **" + selectedModule + "** to **" + e.getTextChannel().getName() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            } else if(res == 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully removed binding of **" + selectedModule + "** from **" + e.getTextChannel().getName() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }

    }

}
