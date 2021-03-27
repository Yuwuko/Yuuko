package com.yuuko.modules.core.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.Module;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", 0, -1L, Arrays.asList("-help", "-help <command>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        // If command length is smaller than 2 give the regular help DM, else give the command usage embed.
        if(!e.hasParameters()) {
            EmbedBuilder commandInfo = new EmbedBuilder()
                    .setTitle("Have an issue, suggestion, or just want me on your server?")
                    .setDescription("Click [here](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) to send me an invite, or [here](https://discord.gg/VsM25fN) to join the support server! " +
                            "\n Stuck with a command? Use `" + e.getPrefix() + "help <command>` to get the commands usage.")
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getAvatarUrl());

            for(Module module: Yuuko.MODULES.values()) {
                commandInfo.addField(module.getName(), module.getCommandsAsString(), false);
            }

            if(e.getGuild().getMemberById(Yuuko.BOT_ID).hasPermission(Permission.MESSAGE_WRITE)) {
                MessageDispatcher.reply(e, commandInfo.build());
            } else {
                e.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());
            }

        } else {
            // Loop through the list of commands until the name of the command matches the help commands parameter given.
            // Once it matches, start to gather the information necessary for the Embed message to be returned to the user.
            Yuuko.COMMANDS.values().stream().filter(command -> command.getName().equalsIgnoreCase(e.getParameters())).findFirst().ifPresent(command -> {
                final String commandPermission = (command.getPermissions() == null) ? "None" : command.getPermissions().toString();

                StringBuilder usages = new StringBuilder();
                for(String usage: command.getUsage()) {
                    usages.append(usage.replace("-", e.getPrefix())).append("\n");
                }
                TextUtilities.removeLast(usages, "\n");

                EmbedBuilder embed = new EmbedBuilder()
                        .setThumbnail(Yuuko.BOT.getAvatarUrl())
                        .setTitle("Command help for **_" + command.getName() + "_**")
                        .addField("Module", command.getModule().getName(), true)
                        .addField("Required Permissions", commandPermission, true)
                        .addField("Binds", BindCommand.DatabaseInterface.getBindsByModule(e.getGuild(), command.getModule().getName(), ", "), true)
                        .addField("Usage", usages.toString(), false)
                        .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getAvatarUrl());
                MessageDispatcher.reply(e, embed.build());
            });
        }
    }

}
