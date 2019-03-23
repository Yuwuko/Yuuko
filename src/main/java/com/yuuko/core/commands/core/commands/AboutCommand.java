package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AboutCommand extends Command {

    public AboutCommand() {
        super("about", CoreModule.class, 0, new String[]{"-about"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        EmbedBuilder about = new EmbedBuilder()
                .setAuthor(Configuration.BOT.getName() + "#" + Configuration.BOT.getDiscriminator(), null, Configuration.BOT.getAvatarUrl())
                .setDescription(
                        Configuration.BOT.getName() + ", programmed in [Java](https://www.oracle.com/uk/java/index.html), using [Gradle](https://gradle.org/) for dependencies.  " +
                        "If you would like me in your guild, [invite me!](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) " +
                        "If I already am, thank you for your continued support!"
                )
                .setThumbnail(Configuration.BOT.getAvatarUrl())
                .addField("Author", "[" + Configuration.AUTHOR + "](" + Configuration.AUTHOR_WEBSITE + ")", true)
                .addField("Version", Configuration.VERSION, true)
                .addField("Guilds", MetricsManager.getDiscordMetrics().GUILD_COUNT + "", true)
                .addField("Prefix", Configuration.GLOBAL_PREFIX + ", " + Utilities.getServerPrefix(e.getGuild()), true)
                .addField("Commands", Configuration.COMMANDS.size() + "", true)
                .addField("Modules", Configuration.MODULES.size() + "", true)
                .addField("Uptime", TextUtilities.getTimestamp(MetricsManager.getSystemMetrics().UPTIME), true)
                .addField("Ping", MetricsManager.getDiscordMetrics().PING + "", true);
        MessageHandler.sendMessage(e, about.build());
    }
}
