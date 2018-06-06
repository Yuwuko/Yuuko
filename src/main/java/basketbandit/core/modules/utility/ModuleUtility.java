package basketbandit.core.modules.utility;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.utility.commands.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public class ModuleUtility extends Module {

    public ModuleUtility() {
        super("ModuleUtility", "moduleUtility");
    }

    /**
     * Module constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    public ModuleUtility(MessageReceivedEvent e, String[] command) {
        super("ModuleUtility", "moduleUtility");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    /**
     * Module constructor for MessageReactionAddEvents
     * @param e MessageReactionAddEvent
     */
    public ModuleUtility(MessageReactionAddEvent e) {
        super("ModuleUtility", "moduleUtility");

        if(e.getReaction().getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            Message message = e.getTextChannel().getMessageById(e.getMessageId()).complete();
            message.pin().queue();
        }
    }

    /**
     * Module constructor for MessageReactionRemoveEvents
     * @param e MessageReactionRemoveEvent
     */
    public ModuleUtility(MessageReactionRemoveEvent e) {
        super("ModuleUtility", "moduleUtility");

        Message message = e.getTextChannel().getMessageById(e.getMessageId()).complete();

        if(e.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            for(MessageReaction emote: message.getReactions()) {
                if(emote.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
                    return;
                }
            }
            message.unpin().queue();
        }
    }

    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.USER.getCommandName())) {
            new CommandUser(e, command);
            return;
        }

        if(command[0].equals(C.SERVER.getCommandName())) {
            new CommandServer(e, command);
            return;
        }

        if(command[0].equals(C.BIND.getCommandName())) {
            new CommandBind(e, command);
            return;
        }

        if(command[0].equals(C.UNBIND.getCommandName())) {
            new CommandUnbind(e, command);
            return;
        }

        if(command[0].equals(C.EXCLUDE.getCommandName())) {
            new CommandExclude(e, command);
            return;
        }

        if(command[0].equals(C.INCLUDE.getCommandName())) {
            new CommandUnexclude(e, command);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.").queue();
    }
}