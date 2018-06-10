package com.basketbandit.core.modules.moderation.commands;

import com.basketbandit.core.Utils;
import com.basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandUnmute extends Command {

    public CommandUnmute() {
        super("unmute", "com.basketbandit.core.modules.moderation.ModuleModeration", new String[]{"-unmute @user"}, Permission.VOICE_MUTE_OTHERS);
    }

    public CommandUnmute(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
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

        e.getGuild().getController().setMute(target, false).queue();
        Utils.sendMessage(e,target.getAsMention() + " has been muted.");
    }
}
