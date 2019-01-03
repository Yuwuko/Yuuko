package com.yuuko.core.modules.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.ResultSet;

import static com.yuuko.core.database.DatabaseConnection.getConnection;

public class CommandHelp extends Command {

    public CommandHelp() {
        super("help", "com.yuuko.core.modules.core.ModuleCore", 0, new String[]{"-help", "-help [command]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] cmd) {
        // If command length is smaller than 2 give the regular help DM, else give the command usage embed.
        if(cmd.length < 2) {
            EmbedBuilder commandInfo = new EmbedBuilder()
                    .setTitle("Have an issue, suggestion or just want me on your server?")
                    .setDescription("Click [here](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) to send me an invite, or [here](https://discord.gg/QcwghsA) to join the support server! If you want a description of a command you can find it [here](https://www.yuuko.info)!")
                    .addField("Stuck with a command?", "Use `-help <command>` to get usage.", false)
                    .addField("Core", "`about` `help` `module` `modules` `settings`", false)
                    .addField("Audio", "`play` `pause` `background` `clear` `current` `last` `search` `queue` `repeat` `shuffle` `skip` `stop`", false)
                    .addField("Math", "`roll`", false)
                    .addField("Media", "`kitsu` `osu`", false)
                    .addField("Moderation", "`ban` `kick` `mute` `nuke`", false)
                    .addField("World", "`linestatus` `weather` `tesco`", false)
                    .addField("Utility", "`bind` `channel` `server` `user`", false)
                    .addField("NSFW", "`efukt` `neko`", false)
                    .setFooter(Cache.STANDARD_STRINGS[0], e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

            EmbedBuilder embed = new EmbedBuilder().setTitle(e.getAuthor().getName()).setDescription("Information has been sent to your direct messages.");
            MessageHandler.sendMessage(e, embed.build());

            e.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());

        } else {
            // Loop through the list of commands until the name of the command matches the help commands parameter given.
            // Once it matches, start to gather the information necessary for the Embed message to be returned to the user.
            Cache.COMMANDS.stream().filter(command -> command.getCommandName().equalsIgnoreCase(cmd[1])).findFirst().ifPresent(command -> {

                final String commandPermission;
                if(command.getCommandPermissions() == null) {
                    commandPermission = "None";
                } else {
                    commandPermission = Utils.getCommandPermissions(command.getCommandPermissions());
                }

                StringBuilder usages = new StringBuilder();
                for(String usage: command.getCommandUsage()) {
                    usages.append(usage).append("\n");
                }
                Utils.removeLastOccurrence(usages, "\n");

                // Connect to the database, grab results about bindings for a certain command and return them.
                // If the command is excluded add it to the first list, if it is bound add it to the second list.
                // In the event that there are no bindings, just return "none".
                StringBuilder bindList = new StringBuilder();
                try {
                    Connection connection = getConnection();
                    ResultSet rs = new DatabaseFunctions().getBindingsByModule(connection, e.getGuild().getId(), Utils.extractModuleName(command.getCommandModule(), false, true));
                    while(rs.next()) {
                        bindList.append(e.getGuild().getTextChannelCache().getElementById(rs.getString(2)).getName()).append("\n");
                    }
                    connection.close();

                    Utils.removeLastOccurrence(bindList, "\n");

                    if(bindList.length() == 0) {
                        bindList.append("None");
                    }
                } catch(Exception ex) {
                    MessageHandler.sendException(ex, e.getMessage().getContentRaw());
                }

                EmbedBuilder embed = new EmbedBuilder()
                        .setThumbnail(Cache.BOT.getAvatarUrl())
                        .setTitle("Command help for **_" + command.getCommandName() + "_**")
                        .addField("Module", Utils.extractModuleName(command.getCommandModule(), true, false), true)
                        .addField("Required Permissions", commandPermission, true)
                        .addField("Binds", bindList.toString(), true)
                        .addField("Usage", usages.toString(), false)
                        .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                MessageHandler.sendMessage(e, embed.build());
            });

            EmbedBuilder embed = new EmbedBuilder().setTitle("Usage not found for command '" + cmd[1] + "'.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
