package com.yuuko.core.modules.moderation.commands;

import com.yuuko.core.modules.Command;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandUnmute extends Command {

    public CommandUnmute() {
        super("unmute", "com.yuuko.core.modules.moderation.ModuleModeration",1, new String[]{"-unmute @user"}, Permission.VOICE_MUTE_OTHERS);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        List<Member> mentioned = e.getMessage().getMentionedMembers();
        Member target;

        if(mentioned.size() > 0) {
            target = mentioned.get(0);
        } else {
            target = e.getGuild().getMemberById(Long.parseLong(command[1]));
        }

        if(target == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("That user could not found.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle(target.getAsMention() + " has been unmuted.");
        MessageHandler.sendMessage(e, embed.build());
        e.getGuild().getController().removeSingleRoleFromMember(target, Utils.setupMutedRole(e.getGuild())).queue();
    }

}
