// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core;

import basketbandit.core.modules.*;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

import java.time.Instant;

public class Controller {

    private Database database;
    private String serverLong;

    /**
     * Controller constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    public Controller(MessageReceivedEvent e, Database d, BasketBandit b) {
        this.database = d;
        String[] command = e.getMessage().getContentRaw().split("\\s+", 2);
        User user = e.getAuthor();
        serverLong = e.getGuild().getIdLong() + "";

        // Remove the command message.
        e.getMessage().delete().complete();

        System.out.println("[" + Thread.currentThread().getName() + "] - " + Instant.now() + " - " + e.getGuild().getName() + " - " + command[0]);

        try {
            // Command switch -> chooses which module class to sent the message event to.
            switch(command[0].toLowerCase()) {
                case Configuration.PREFIX + "module":
                case Configuration.PREFIX + "modules":
                case Configuration.PREFIX + "setup":
                case Configuration.PREFIX + "help":
                    new ModuleCore(e);
                    break;

                // ModuleDev
                case Configuration.PREFIX + "dbsetup":
                    if(user.getIdLong() == 215161101460045834L || database.checkModuleSettings("modDev", serverLong)) {
                        new ModuleDev(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the dev module is disabled.").queue();
                    }
                    break;

                // ModuleModeration
                case Configuration.PREFIX + "nuke":
                case Configuration.PREFIX + "kick":
                case Configuration.PREFIX + "ban":
                case Configuration.PREFIX + "addchannel":
                case Configuration.PREFIX + "delchannel":
                    if(database.checkModuleSettings("modModeration", serverLong)) {
                        new ModuleModeration(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the moderation module is disabled.").queue();
                    }
                    break;

                // ModuleUtility
                case Configuration.PREFIX + "server":
                case Configuration.PREFIX + "user":
                    if(database.checkModuleSettings("modUtility", serverLong)) {
                        new ModuleUtility(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the utility module is disabled.").queue();
                    }
                    break;

                // ModuleMath
                case Configuration.PREFIX + "roll":
                case Configuration.PREFIX + "sum":
                    if(database.checkModuleSettings("modMath", serverLong)) {
                        new ModuleMath(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the math module is disabled.").queue();
                    }
                    break;

                // ModuleFun
                case Configuration.PREFIX + "insult":
                case Configuration.PREFIX + "overreact":
                    if(database.checkModuleSettings("modFun", serverLong)) {
                        new ModuleFun(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the fun module is disabled.").queue();
                    }
                    break;

                // ModuleRuneScape
                case Configuration.PREFIX + "rsstats":
                case Configuration.PREFIX + "osstats":
                    if(database.checkModuleSettings("modRuneScape", serverLong)) {
                        new ModuleRuneScape(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the runescape module is disabled.").queue();
                    }
                    break;

                // ModuleMusic
                case Configuration.PREFIX + "play":
                case Configuration.PREFIX + "stop":
                case Configuration.PREFIX + "pause":
                case Configuration.PREFIX + "track":
                case Configuration.PREFIX + "skip":
                case Configuration.PREFIX + "shuffle":
                case Configuration.PREFIX + "queue":
                case Configuration.PREFIX + "setbackground":
                case Configuration.PREFIX + "unsetbackground":
                    if(database.checkModuleSettings("modMusic", serverLong)) {
                        new ModuleMusic(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the music module is disabled.").queue();
                    }
                    break;

                    // ModuleCustom
                case Configuration.PREFIX + "addcc":
                case Configuration.PREFIX + "delcc":
                    if(database.checkModuleSettings("modCustom", serverLong)) {
                        new ModuleCustom(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the custom module is disabled.").queue();
                    }
                    break;

                default:
                    if(database.checkModuleSettings("modCustom", serverLong)) {
                        new ModuleCustom(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the custom module is disabled.").queue();
                    }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ModuleLogging
        if(database.checkModuleSettings("modLogging", serverLong)) {
            new ModuleLogging(e);
        }

    }

    /**
     * Controller constructor for MessageReactionAddEvents
     * @param e MessageReactionAddEvent
     */
    public Controller(MessageReactionAddEvent e) {
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
    public Controller(MessageReactionRemoveEvent e) {
        MessageReaction react = e.getReaction();
        serverLong = e.getGuild().getIdLong() + "";

        if(react.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            if(database.checkModuleSettings("modUtility", serverLong)) {
                new ModuleUtility(e);
            }
        }
    }
}
