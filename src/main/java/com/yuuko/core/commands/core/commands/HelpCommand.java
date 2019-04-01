package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.database.BindFunctions;
import com.yuuko.core.database.CommandFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", CoreModule.class, 0, new String[]{"-help", "-help <command>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        // If command length is smaller than 2 give the regular help DM, else give the command usage embed.
        if(e.getCommand().length < 2) {
            EmbedBuilder commandInfo = new EmbedBuilder()
                    .setTitle("Have an issue, suggestion, or just want me on your server?")
                    .setDescription("Click [here](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) to send me an invite, or [here](https://discord.gg/VsM25fN) to join the support server! If you want a description of a command you can find it [here](https://www.yuuko.info)!")
                    .addField("Stuck with a command?", "Use `" + e.getPrefix() + "help <command>` to get usage.", false)
                    .setFooter(Configuration.STANDARD_STRINGS[0], Configuration.BOT.getAvatarUrl());

            for(Module module: Configuration.MODULES) {
                commandInfo.addField(module.getName(), module.getCommandsAsString(), false);
            }

            if(e.getGuild().getMemberById(Configuration.BOT_ID).hasPermission(Permission.MESSAGE_WRITE)) {
                MessageHandler.sendMessage(e, commandInfo.build());
            } else {
                e.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());
            }

        } else {
            // Loop through the list of commands until the name of the command matches the help commands parameter given.
            // Once it matches, start to gather the information necessary for the Embed message to be returned to the user.
            Configuration.COMMANDS.stream().filter(command -> command.getName().equalsIgnoreCase(e.getCommand()[1])).findFirst().ifPresent(command -> {
                final String commandPermission;
                if(command.getPermissions() == null) {
                    commandPermission = "None";
                } else {
                    commandPermission = Utilities.getCommandPermissions(command.getPermissions());
                }

                StringBuilder usages = new StringBuilder();
                for(String usage: command.getUsage()) {
                    usages.append(usage).append("\n");
                }
                TextUtilities.removeLastOccurrence(usages, "\n");

                String disabled = TextUtilities.formatArray(CommandFunctions.getCommandSetting(e.getGuild(), command).toString());

                EmbedBuilder embed = new EmbedBuilder()
                        .setThumbnail(Configuration.BOT.getAvatarUrl())
                        .setTitle("Command help for **_" + command.getName() + "_**")
                        .addField("Module", command.getModule().getSimpleName().substring(0, command.getModule().getSimpleName().length() - 6), true)
                        .addField("Required Permissions", commandPermission, true)
                        .addField("Binds", BindFunctions.getBindsByModule(e.getGuild(), command.getModule().getName(), ", "), true)
                        .addField("Disabled", (disabled.equals("")) ? "None" : disabled, true)
                        .addField("Usage", usages.toString().replace("-", e.getPrefix()), false)
                        .setFooter(Configuration.STANDARD_STRINGS[0], Configuration.BOT.getAvatarUrl());
                MessageHandler.sendMessage(e, embed.build());
            });
        }
    }

}
