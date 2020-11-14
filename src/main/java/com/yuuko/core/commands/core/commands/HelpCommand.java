package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.database.function.BindFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", Config.MODULES.get("core"), 0, -1L, Arrays.asList("-help", "-help <command>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        // If command length is smaller than 2 give the regular help DM, else give the command usage embed.
        if(!e.hasParameters()) {
            EmbedBuilder commandInfo = new EmbedBuilder()
                    .setTitle("Have an issue, suggestion, or just want me on your server?")
                    .setDescription("Click [here](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) to send me an invite, or [here](https://discord.gg/VsM25fN) to join the support server! " +
                            "\n Stuck with a command? Use `" + e.getPrefix() + "help <command>` to get the commands usage.")
                    .setFooter(Config.STANDARD_STRINGS.get(0), Config.BOT.getAvatarUrl());

            for(Module module: Config.MODULES.values()) {
                commandInfo.addField(module.getName(), module.getCommandsAsString(), false);
            }

            if(e.getGuild().getMemberById(Config.BOT_ID).hasPermission(Permission.MESSAGE_WRITE)) {
                MessageHandler.sendMessage(e, commandInfo.build());
            } else {
                e.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());
            }

        } else {
            // Loop through the list of commands until the name of the command matches the help commands parameter given.
            // Once it matches, start to gather the information necessary for the Embed message to be returned to the user.
            Config.COMMANDS.values().stream().filter(command -> command.getName().equalsIgnoreCase(e.getParameters())).findFirst().ifPresent(command -> {
                final String commandPermission = (command.getPermissions() == null) ? "None" : Utilities.getCommandPermissions(command.getPermissions());

                StringBuilder usages = new StringBuilder();
                for(String usage: command.getUsage()) {
                    usages.append(usage.replace("-", e.getPrefix())).append("\n");
                }
                TextUtilities.removeLast(usages, "\n");

                EmbedBuilder embed = new EmbedBuilder()
                        .setThumbnail(Config.BOT.getAvatarUrl())
                        .setTitle("Command help for **_" + command.getName() + "_**")
                        .addField("Module", command.getModule().getName(), true)
                        .addField("Required Permissions", commandPermission, true)
                        .addField("Binds", BindFunctions.getBindsByModule(e.getGuild(), command.getModule().getName(), ", "), true)
                        .addField("Usage", usages.toString(), false)
                        .setFooter(Config.STANDARD_STRINGS.get(0), Config.BOT.getAvatarUrl());
                MessageHandler.sendMessage(e, embed.build());
            });
        }
    }

}
