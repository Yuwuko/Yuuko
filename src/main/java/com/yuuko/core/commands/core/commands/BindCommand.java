package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.BindFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", Config.MODULES.get("core"), 0, -1L, Arrays.asList("-bind <module>", "-bind <module> #channel..."), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(e.hasParameters()) {
            String[] params = e.getParameters().toLowerCase().split("\\s+", 2);

            if(!params[0].equals("*") && !Config.MODULES.containsKey(params[0])) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("**" + params[0] + "** isn't a valid module. A list of valid module can be found by using the **" + Utilities.getServerPrefix(e.getGuild()) + "help** command.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            String module = params[0].equals("*") ? "*" : Config.MODULES.get(params[0]).getName();

            if(params.length > 1) {
                List<TextChannel> channels = e.getMessage().getMentionedChannels();

                if(channels.size() < 1) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Expected at least **1** \\#tagged channel to bind, found **0**.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                StringBuilder boundChannels = new StringBuilder();
                for(TextChannel channel : channels) {
                    BindFunctions.toggleBind(e.getGuild().getId(), channel.getId(), module);
                    boundChannels.append(channel.getName()).append(", ");
                }
                TextUtilities.removeLast(boundChannels, ", ");

                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully toggled **" + module + "** on **" + boundChannels.toString() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            } else {
                final int res = BindFunctions.toggleBind(e.getGuild().getId(), e.getChannel().getId(), module);

                if(res == 0) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully bound **" + module + "** to **" + e.getChannel().getName() + "**.");
                    MessageHandler.sendMessage(e, embed.build());
                } else if(res == 1) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully removed binding of **" + module + "** from **" + e.getChannel().getName() + "**.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
        } else {
            try {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Bound Modules")
                        .setDescription(BindFunctions.getGuildBinds(e.getGuild(), "\n"));
                MessageHandler.sendMessage(e, embed.build());
            } catch(Exception ex) {
                //
            }
        }

    }

}
