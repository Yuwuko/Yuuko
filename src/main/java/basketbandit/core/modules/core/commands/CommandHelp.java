package basketbandit.core.modules.core.commands;

import basketbandit.core.Configuration;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandHelp extends Command {

    public CommandHelp() {
        super("help", "basketbandit.core.modules.core.ModuleCore", null);
    }

    public CommandHelp(MessageReceivedEvent e) {
        super("help", "basketbandit.core.modules.core.ModuleCore", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        EmbedBuilder commandInfo = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Hey " + e.getAuthor().getName() + ",")
                .setDescription(
                        "A full list of modules and features is available on my GitHub, which is located [here](https://github.com/Galaxiosaurus/BasketBandit)! \n" +
                        "If you would like to suggest new features or have any general comments you can send them to my creator [here](https://discord.gg/QcwghsA)! \n\n" +

                        "P.S, modules used to be listed here but formatting is a pain and nobody has time for that."
                )
                .addField("Want me on your server?", "Click [here](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) to send me an invite! Also be sure to give me admin privileges if you wish to use the " + Configuration.PREFIX + "nuke command or any other admin modules.", false)
                .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl())
                ;
        e.getTextChannel().sendMessage("Check your private messages, " + e.getAuthor().getAsMention() + "! <:ShinobuOshino:420423622663077889>").queue();
        e.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());
    }
}
