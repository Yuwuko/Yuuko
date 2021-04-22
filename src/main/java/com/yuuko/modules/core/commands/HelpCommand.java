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
        super("help", Arrays.asList("-help", "-help <command>"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        // If command length is smaller than 2 give the regular help DM, else give the command usage embed.
        if(!context.hasParameters()) {
            EmbedBuilder commandInfo = new EmbedBuilder()
                    .setTitle(context.i18n( "help_title"))
                    .setDescription(context.i18n( "help_desc").formatted("https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot", Yuuko.SUPPORT_GUILD, context.getPrefix()))
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getAvatarUrl());

            for(Module module: Yuuko.MODULES.values()) {
                commandInfo.addField(module.getName(), module.getCommandsAsString(), false);
            }

            if(context.getGuild().getMemberById(Yuuko.BOT_ID).hasPermission(Permission.MESSAGE_WRITE)) {
                MessageDispatcher.reply(context, commandInfo.build());
            } else {
                context.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());
            }

        } else {
            // Loop through the list of commands until the name of the command matches the help commands parameter given.
            // Once it matches, start to gather the information necessary for the Embed message to be returned to the user.
            Yuuko.COMMANDS.values().stream().filter(command -> command.getName().equalsIgnoreCase(context.getParameters())).findFirst().ifPresent(command -> {
                final String commandPermission = (command.getPermissions() == null) ? context.i18n( "none") : command.getPermissions().toString();

                StringBuilder usages = new StringBuilder();
                for(String usage: command.getUsage()) {
                    usages.append(usage.replace("-", context.getPrefix())).append("\n");
                }
                TextUtilities.removeLast(usages, "\n");

                EmbedBuilder embed = new EmbedBuilder()
                        .setThumbnail(Yuuko.BOT.getAvatarUrl())
                        .setTitle(context.i18n( "title").formatted(command.getName()))
                        .addField(context.i18n( "module"), command.getModule().getName(), true)
                        .addField(context.i18n( "perms"), commandPermission, true)
                        .addField(context.i18n( "binds"), BindCommand.DatabaseInterface.getBindsByModule(context.getGuild(), command.getModule().getName(), ", "), true)
                        .addField(context.i18n( "usage"), usages.toString(), false)
                        .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getAvatarUrl());
                MessageDispatcher.reply(context, embed.build());
            });
        }
    }

}
