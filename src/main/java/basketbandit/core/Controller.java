package basketbandit.core;

import basketbandit.core.commands.Command;
import basketbandit.core.commands.CommandPlay;
import basketbandit.core.modules.ModuleCustom;
import basketbandit.core.modules.ModuleLogging;
import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.modules.ModuleUtility;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.ArrayList;

class Controller {

    private Database database = new Database();
    private String serverLong;

    /**
     * Controller constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    Controller(MessageReceivedEvent e, ArrayList<Command> commandList) {
        String[] input = e.getMessage().getContentRaw().split("\\s+", 2);
        serverLong = e.getGuild().getIdLong() + "";
        boolean executed = false;

        // Remove the input message.
        e.getMessage().delete().complete();

        System.out.println("[" + Thread.currentThread().getName() + "] - " + Instant.now() + " - " + e.getGuild().getName() + " - " + input[0]);

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
                if(ModuleMusic.searchUsers.containsKey(e.getAuthor().getIdLong()) && !input[0].equals("cancel")) {
                    // -1 because arrays start at 0.
                    new CommandPlay(e, ModuleMusic.searchUsers.get(e.getAuthor().getIdLong()).get(Integer.parseInt(input[0]) - 1).getId().getVideoId());

                } else if(ModuleMusic.searchUsers.containsKey(e.getAuthor().getIdLong()) && input[0].equals("cancel")) {
                    ModuleMusic.searchUsers.remove(e.getAuthor().getIdLong());
                    e.getTextChannel().sendMessage("(" + e.getAuthor().getAsMention() + ") Search cancelled.").queue();

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
