package com.yuuko.core.modules.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.core.CoreModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.TextUtility;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AboutCommand extends Command {

    public AboutCommand() {
        super("about", CoreModule.class, 0, new String[]{"-about"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        EmbedBuilder about = new EmbedBuilder()
                .setAuthor(Cache.BOT.getName() + "#" + Cache.BOT.getDiscriminator(), null, Cache.BOT.getAvatarUrl())
                .setDescription(Cache.BOT.getName() + ", programmed in " +
                        "[Java](https://www.oracle.com/uk/java/index.html), using [Gradle](https://gradle.org/) for dependencies.  " +
                        "If you would like me in your guild, [invite me!](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) " +
                        "If I already am, thank you for your continued support!"
                )
                .setThumbnail(Cache.BOT.getAvatarUrl())
                .addField("Author", "[" + Configuration.AUTHOR + "](" + Configuration.AUTHOR_WEBSITE + ")", true)
                .addField("Version", Configuration.VERSION, true)
                .addField("Guilds", MetricsManager.getDiscordMetrics().GUILD_COUNT + "", true)
                .addField("Prefix", Configuration.GLOBAL_PREFIX + ", " + Utils.getServerPrefix(e.getGuild().getId()), true)
                .addField("Commands", Cache.COMMANDS.size() + "", true)
                .addField("Modules", Cache.MODULES.size() + "", true)
                .addField("Uptime", TextUtility.getTimestamp(MetricsManager.getSystemMetrics().UPTIME), true)
                .addField("Ping", MetricsManager.getDiscordMetrics().PING + "", true);
        MessageHandler.sendMessage(e, about.build());
    }
}
