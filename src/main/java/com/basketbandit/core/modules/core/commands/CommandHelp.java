package com.basketbandit.core.modules.core.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.SystemInformation;
import com.basketbandit.core.database.DatabaseConnection;
import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;

public class CommandHelp extends Command {

    public CommandHelp() {
        super("help", "com.basketbandit.core.modules.core.ModuleCore", 0, new String[]{"-help", "-help [command]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        // If command length is smaller than 2 give the regular help DM, else give the command usage embed.
        if(command.length < 2) {
            EmbedBuilder commandInfo = new EmbedBuilder()
                    .setColor(Color.DARK_GRAY)
                    .setTitle("**Have an issue, suggestion or just want me on your server?**")
                    .setDescription("Click [here](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) to send me an invite, or [here](https://discord.gg/QcwghsA) to join the support server! If you want a description of a command you can find it [here](https://github.com/BasketBandit/BasketBandit-Java/blob/master/README.md)!")
                    .addField("Stuck with a command?", "Use `-help <command>` to get usage.", false)
                    .addField("Core", "`about` `help` `module` `modules` `settings` `setup`", false)
                    .addField("Audio", "`play` `pause` `background` `clear` `current` `last` `search` `queue` `repeat` `shuffle` `skip` `stop`", false)
                    .addField("Math", "`roll` `sum`", false)
                    .addField("Media", "`kitsu` `osu` `runescape` `wow`", false)
                    .addField("Moderation", "`ban` `kick` `mute` `unmute` `nuke`", false)
                    .addField("World", "`linestatus` `weather`", false)
                    .addField("Utility", "`bind` `exclude` `channel` `server` `users`", false)
                    .addField("NSFW", "`efukt` `neko`", false)
                    .setFooter(Utils.getStandardString(0), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

            MessageHandler.sendMessage(e, "Check your private messages, " + e.getAuthor().getAsMention() + "! <:ShinobuOshino:420423622663077889>");

            e.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());

        } else {
            // Loop through the list of commands until the name of the command matches the help commands parameter given.
            // Once it matches, start to gather the information necessary for the Embed message to be returned to the user.
            for(Command cmd: SystemInformation.getCommandList()) {
                if(cmd.getCommandName().equals(command[1])) {
                    String commandPermission;
                    if(cmd.getCommandPermission() == null) {
                        commandPermission = "None";
                    } else {
                        commandPermission = cmd.getCommandPermission().getName();
                    }

                    StringBuilder usages = new StringBuilder();
                    for(String usage: cmd.getCommandUsage()) {
                        usages.append(usage).append("\n");
                    }
                    usages = Utils.removeLastOccurrence(usages, "\n");

                    StringBuilder bindList = new StringBuilder();
                    StringBuilder excludeList = new StringBuilder();

                    // Connect to the database, grab results about bindings and exclusions for a certain command and return them.
                    // If the command is excluded add it to the first list, if it is bound add it to the second list.
                    // In the event that there are no exclusions nor bindings, just return "none".
                    try {
                        Connection connection = new DatabaseConnection().getConnection();
                        ResultSet rs = new DatabaseFunctions().getBindingsExclusionsChannel(connection, e.getGuild().getId(), Utils.extractModuleName(cmd.getCommandModule(), false, true));
                        while(rs.next()) {
                            if(rs.getBoolean(5)) {
                                excludeList.append(e.getGuild().getTextChannelCache().getElementById(rs.getString(2)).getName()).append("\n");
                            } else {
                                bindList.append(e.getGuild().getTextChannelCache().getElementById(rs.getString(2)).getName()).append("\n");
                            }
                        }
                        connection.close();

                        Utils.removeLastOccurrence(bindList, "\n");
                        Utils.removeLastOccurrence(excludeList, "\n");
                        if(bindList.length() == 0) {
                            bindList.append("None");
                        }
                        if(excludeList.length() == 0) {
                            excludeList.append("None");
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }

                    User bot = e.getGuild().getMemberById(420682957007880223L).getUser();

                    EmbedBuilder commandInfo = new EmbedBuilder()
                            .setColor(Color.DARK_GRAY)
                            .setThumbnail(bot.getAvatarUrl())
                            .setTitle("Command help for **" + cmd.getCommandName() + "**")
                            .addField("Module", Utils.extractModuleName(cmd.getCommandModule(), true, false), true)
                            .addField("Required Permission", commandPermission, true)
                            .addField("Binds", bindList.toString(), true)
                            .addField("Exclusions", excludeList.toString(), true)
                            .addField("Usage", usages.toString(), false)
                            .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                    MessageHandler.sendMessage(e, commandInfo.build());
                    return;
                }
            }
            MessageHandler.sendMessage(e, "Sorry, I can't find a usage for command '" + command[1] + "'");
        }
    }

}
