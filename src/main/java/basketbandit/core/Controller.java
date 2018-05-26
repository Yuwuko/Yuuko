package basketbandit.core;

import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.ModuleAudio;
import basketbandit.core.modules.audio.commands.CommandPlay;
import basketbandit.core.modules.custom.ModuleCustom;
import basketbandit.core.modules.logging.ModuleLogging;
import basketbandit.core.modules.utility.ModuleUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class Controller {

    private Database database = new Database();
    private String serverLong;

    /**
     * Controller constructor for GuildJoinEvents
     * @param e GuildJoinEvent
     */
    Controller(GuildJoinEvent e) {
        List<TextChannel> channels = e.getGuild().getTextChannels();
        User bot = e.getGuild().getMemberById(420682957007880223L).getUser();

        int users = 0;
        for(Guild guild : bot.getJDA().getGuilds()) {
            users += guild.getMemberCache().size();
        }

        EmbedBuilder about = new EmbedBuilder()
                .setColor(Color.WHITE)
                .setAuthor(bot.getName() + "#" + bot.getDiscriminator(), null, bot.getAvatarUrl())
                .setDescription("Thanks for inviting me to your server! Below is a little bit of information about myself, and you can access a list of my modules [here](!")
                .setThumbnail(bot.getAvatarUrl())
                .addField("Author", "[0x00000000#0001](https://github.com/Galaxiosaurus/)", true)
                .addField("Version", Configuration.VERSION, true)
                .addField("Servers", bot.getJDA().getGuilds().size()+"", true)
                .addField("Users", users+"", true)
                .addField("Commands", "35", true)
                .addField("Invocation", Configuration.PREFIX, true)
                .addField("Uptime", TimeKeeper.runtime, true)
                .addField("Ping", bot.getJDA().getPing()+"", true);

        for(TextChannel c: channels) {
            if(c.getName().toLowerCase().equals("general")) {
                c.sendMessage(about.build()).queue();
                break;
            }
        }

        e.getGuild().getOwner().getUser().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(about.build()).queue());
    }

    /**
     * Controller constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    Controller(MessageReceivedEvent e, ArrayList<Command> commandList) {
        String[] input = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        serverLong = e.getGuild().getIdLong() + "";
        boolean executed = false;

        // Remove the input message.
        e.getMessage().delete().complete();

        System.out.println("[" + Thread.currentThread().getName() + "] " + Instant.now() + " - " + e.getGuild().getName() + " - " + input[0]);

        try {
            // Iterate through the command list, if the input matches the effective name (includes invocation)
            // find the module class that belongs to the command itself and create a new instance of that
            // constructor (which takes a MessageReceivedEvent) with the parameter of a MessageReceivedEvent.
            for(Command c: commandList) {
                if(input[0].equals(c.getEffectiveName())) {
                    Class<?> clazz = Class.forName(c.getCommandModule());
                    Constructor<?> constructor = clazz.getConstructor(MessageReceivedEvent.class);
                    constructor.newInstance(e);
                    executed = true;
                    break;
                }
            }

            // Search function check if regex matches. Used in conjunction with the search input.
            if(input[0].matches("^[0-9]{1,2}$") || input[0].equals("cancel")) {
                if(!input[0].equals("cancel") && ModuleAudio.searchUsers.containsKey(e.getAuthor().getIdLong())) {
                    new CommandPlay(e, ModuleAudio.searchUsers.get(e.getAuthor().getIdLong()).get(Integer.parseInt(input[0]) - 1).getId().getVideoId());

                } else if(input[0].equals("cancel") && ModuleAudio.searchUsers.containsKey(e.getAuthor().getIdLong())) {
                    ModuleAudio.searchUsers.remove(e.getAuthor().getIdLong());
                    e.getTextChannel().sendMessage("[" + e.getAuthor().getAsMention() + "] Search cancelled.").queue();

                }
            }

            if(!executed && input[0].startsWith(Configuration.PREFIX + Configuration.PREFIX)) {
                if(database.checkModuleSettings("modCustom", serverLong)) {
                    new ModuleCustom(e);
                } else {
                    e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the custom module is disabled.").queue();
                }
            }

            if(database.checkModuleSettings("modLogging", serverLong)) {
                new ModuleLogging(e);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Controller constructor for MessageReactionAddEvents
     * @param e MessageReactionAddEvent
     */
    Controller(MessageReactionAddEvent e) {
        MessageReaction react = e.getReaction();
        serverLong = e.getGuild().getIdLong() + "";

        if(react.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            if(database.checkModuleSettings("modUtility", serverLong)) {
                new ModuleUtility(e);
            }
        }
    }

    /**
     * Controller constructor for MessageReactionRemoveEvents
     * @param e MessageReactionRemoveEvent
     */
    Controller(MessageReactionRemoveEvent e) {
        MessageReaction react = e.getReaction();
        serverLong = e.getGuild().getIdLong() + "";

        if(react.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            if(database.checkModuleSettings("modUtility", serverLong)) {
                new ModuleUtility(e);
            }
        }
    }
}
