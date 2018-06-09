package basketbandit.core.modules.core.commands;

import basketbandit.core.Configuration;
import basketbandit.core.Utils;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandHelp extends Command {

    public CommandHelp() {
        super("help", "basketbandit.core.modules.core.ModuleCore", new String[]{"-help", "-help [command]"}, null);
    }

    public CommandHelp(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command.length < 2) {
            EmbedBuilder commandInfo = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle("Hey " + e.getAuthor().getName() + ",")
                    .setDescription(
                            "A full list of modules and features is available on my GitHub, which is located [here](https://github.com/BasketBandit/BasketBandit-Java)! \n" +
                                    "If you would like to suggest new features or have any general comments you can send them to my creator [here](https://discord.gg/QcwghsA)! \n\n" +

                                    "P.S, modules used to be listed here but formatting is a pain and nobody has time for that."
                    )
                    .addField("Want me on your server?", "Click [here](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) to send me an invite! Also be sure to give me admin privileges if you wish to use the 'nuke' command or any other admin commands.", false)
                    .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

            Utils.sendMessage(e, "Check your private messages, " + e.getAuthor().getAsMention() + "! <:ShinobuOshino:420423622663077889>");
            e.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());

        } else {
            for(Command cmd: Utils.commandList) {
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
                    usages = Utils.removeLastOccurance(usages, "\n");

                    EmbedBuilder commandInfo = new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle(cmd.getCommandName())
                            .addField("Module", Utils.extractModuleName(cmd.getCommandModule(), true), true)
                            .addField("Required Permission", commandPermission, true)
                            .setDescription(usages.toString())
                            .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                    Utils.sendMessage(e, commandInfo.build());
                    break;
                }
            }
        }
    }
}
