// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public class Controller {

    private Database database = new Database();
    private String serverLong;

    /**
     * Controller constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    public Controller(MessageReceivedEvent e) {
        String[] command = e.getMessage().getContentRaw().split("\\s+", 2);

        System.out.println(Thread.currentThread().getName() + ": " + e.getGuild().getName() + " - " + command[0]);

        // Remove the command message and then get the last 10 messages.
        e.getMessage().delete().complete();

        User user = e.getAuthor();
        serverLong = e.getGuild().getIdLong() + "";

        // Command switch -> chooses which module class to sent the message event to.
        switch(command[0].toLowerCase()) {
            case Configuration.PREFIX + "module":
            case Configuration.PREFIX + "modules":
            case Configuration.PREFIX + "setup":
            case Configuration.PREFIX + "help":
                new ModuleCore(e);
                break;

            case Configuration.PREFIX + "dbsetup":
                if(user.getIdLong() == 215161101460045834L || database.checkModuleSettings("modDev", serverLong)) {
                    new ModuleDev(e);
                } else {
                    e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the dev module is disabled.").queue();
                }
                break;

            case Configuration.PREFIX + "nuke":
            case Configuration.PREFIX + "info":
                if(database.checkModuleSettings("modUtility", serverLong)) {
                    new ModuleUtility(e);
                } else {
                    e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the utility module is disabled.").queue();
                }
                break;

            case Configuration.PREFIX + "roll":
            case Configuration.PREFIX + "sum":
                if(database.checkModuleSettings("modMath", serverLong)) {
                    new ModuleMath(e);
                } else {
                    e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the math module is disabled.").queue();
                }
                break;

            case Configuration.PREFIX + "insult":
            case Configuration.PREFIX + "overreact":
                if(database.checkModuleSettings("modFun", serverLong)) {
                    new ModuleFun(e);
                } else {
                    e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the fun module is disabled.").queue();
                }
                break;

            case Configuration.PREFIX + "rsstats":
            case Configuration.PREFIX + "osstats":
                if(database.checkModuleSettings("modRuneScape", serverLong)) {
                    new ModuleRuneScape(e);
                } else {
                    e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the runescape module is disabled.").queue();
                }
                break;

            case Configuration.PREFIX + "newcc":
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
                    e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the utility module is disabled.").queue();
                }
        }

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

        if(react.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            if(database.checkModuleSettings("modUtility", serverLong)) {
                new ModuleUtility(e);
            }
        }
    }
}
