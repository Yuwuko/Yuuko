package com.basketbandit.core.modules.moderation.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandKick extends Command {

    public CommandKick() {
        super("kick", "com.basketbandit.core.modules.moderation.ModuleModeration",1, new String[]{"-kick @user", "-ban @user [reason]"}, Permission.KICK_MEMBERS);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 3);
        List<Member> mentioned = e.getMessage().getMentionedMembers();
        Member target;

        if(mentioned.size() > 0) {
            target = mentioned.get(0);
        } else {
            target = e.getGuild().getMemberById(Long.parseLong(command[1]));
        }

        if(target == null) {
            Utils.sendMessage(e, "Sorry, that user could not be found.");
            return;
        }

        if(commandParameters.length < 3) {
            e.getGuild().getController().kick(target).queue();
        } else {
            e.getGuild().getController().kick(target, commandParameters[1]).queue();
        }
    }

}
