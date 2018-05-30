package basketbandit.core.modules.core.commands;

import basketbandit.core.Configuration;
import basketbandit.core.TimeKeeper;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandAbout extends Command {

    public CommandAbout() {
        super("about", "basketbandit.core.modules.core.ModuleCore", null);
    }

    public CommandAbout(MessageReceivedEvent e) {
        super("about", "basketbandit.core.modules.core.ModuleCore", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        User bot = e.getGuild().getMemberById(420682957007880223L).getUser();

        int users = 0;
        for(Guild guild : bot.getJDA().getGuilds()) {
            users += guild.getMemberCache().size();
        }

        EmbedBuilder about = new EmbedBuilder()
                .setColor(Color.WHITE)
                .setAuthor(bot.getName() + "#" + bot.getDiscriminator(), null, bot.getAvatarUrl())
                .setDescription(bot.getName() + " is a multi-purpose bot for Discord programmed in " +
                        "[Java](https://www.oracle.com/uk/java/index.html) using [Maven](https://maven.apache.org/) " +
                        "for dependencies. Utilising the [JDA](https://github.com/DV8FromTheWorld/JDA) and [LavaPlayer](https://github.com/sedmelluq/lavaplayer) libraries. " +
                        "If you would like me on your guild [invite me!](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=267775062&scope=bot)"
                )
                .setThumbnail(bot.getAvatarUrl())
                .addField("Author", "[0x00000000#0001](https://github.com/Galaxiosaurus/)", true)
                .addField("Version", Configuration.VERSION, true)
                .addField("Servers", bot.getJDA().getGuilds().size()+"", true)
                .addField("Users", users+"", true)
                .addField("Commands", Configuration.COMMAND_COUNT, true)
                .addField("Invocation", Configuration.GLOBAL_PREFIX, true)
                .addField("Uptime", TimeKeeper.runtime, true)
                .addField("Ping", bot.getJDA().getPing()+"", true);

        e.getTextChannel().sendMessage(about.build()).queue();
    }

}
