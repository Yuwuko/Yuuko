package com.basketbandit.core.modules.moderation.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.MessageHandler;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandBan extends Command {

    public CommandBan() {
        super("ban", "com.basketbandit.core.modules.moderation.ModuleModeration", 1, new String[]{"-ban @user [days]", "-ban @user [days] [reason]"}, Permission.BAN_MEMBERS);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 3);
        int time = Integer.parseInt(commandParameters[1]);
        List<Member> mentioned = e.getMessage().getMentionedMembers();
        Member target;

        if(mentioned.size() > 0) {
            target = mentioned.get(0);
        } else {
            target = e.getGuild().getMemberById(Long.parseLong(command[1]));
        }

        if(target == null) {
            MessageHandler.sendMessage(e, "Sorry, that user could not be found.");
            return;
        }

        if(commandParameters.length < 3) {
            e.getGuild().getController().ban(target, time).queue();
        } else {
            e.getGuild().getController().ban(target, time, commandParameters[2]).queue();
        }
    }

}
