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

class Controller {

    private Database database;
    private String serverLong;

    /**
     * Controller constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    Controller(MessageReceivedEvent e, Database d) {
        this.database = d;
        String[] command = e.getMessage().getContentRaw().split("\\s+", 2);
        User user = e.getAuthor();
        serverLong = e.getGuild().getIdLong() + "";

        // Remove the command message.
        e.getMessage().delete().complete();

        System.out.println("[" + Thread.currentThread().getName() + "] - " + Instant.now() + " - " + e.getGuild().getName() + " - " + command[0]);

        try {
            command[0] = command[0].replace(Configuration.PREFIX, "").toLowerCase();

            // Command switch -> chooses which module class to sent the message event to.
            switch(command[0]) {
                case "module":
                case "modules":
                case "setup":
                case "help":
                    new ModuleCore(e);
                    break;

                // ModuleDev
                case "dbsetup":
                case "setstatus":
                    if(user.getIdLong() == 215161101460045834L) {
                        new ModuleDev(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", you don't have permission to do that! <:fiteme:443069990019530763>").queue();
                    }
                    break;

                // ModuleModeration
                case "nuke":
                case "kick":
                case "ban":
                case "addchannel":
                case "delchannel":
                    if(database.checkModuleSettings("modModeration", serverLong)) {
                        new ModuleModeration(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the moderation module is disabled.").queue();
                    }
                    break;

                // ModuleUtility
                case "server":
                case "user":
                    if(database.checkModuleSettings("modUtility", serverLong)) {
                        new ModuleUtility(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the utility module is disabled.").queue();
                    }
                    break;

                // ModuleTransport
                case "linestatus":
                    if(database.checkModuleSettings("modTransport", serverLong)) {
                        new ModuleTransport(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the transport module is disabled.").queue();
                    }
                    break;

                // ModuleMath
                case "roll":
                case "sum":
                    if(database.checkModuleSettings("modMath", serverLong)) {
                        new ModuleMath(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the math module is disabled.").queue();
                    }
                    break;

                // ModuleFun
                case "insult":
                case "overreact":
                    if(database.checkModuleSettings("modFun", serverLong)) {
                        new ModuleFun(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the fun module is disabled.").queue();
                    }
                    break;

                // ModuleRuneScape
                case "rsstats":
                case "osstats":
                    if(database.checkModuleSettings("modRuneScape", serverLong)) {
                        new ModuleRuneScape(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the runescape module is disabled.").queue();
                    }
                    break;

                // ModuleMusic
                case "play":
                case "stop":
                case "pause":
                case "track":
                case "lasttrack":
                case "skip":
                case "shuffle":
                case "queue":
                case "setbackground":
                case "unsetbackground":
                case "togglerepeat":
                    if(database.checkModuleSettings("modMusic", serverLong)) {
                        new ModuleMusic(e);
                    } else {
                        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the music module is disabled.").queue();
                    }
                    break;

                    // ModuleCustom
                case "addcc":
                case "delcc":
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
