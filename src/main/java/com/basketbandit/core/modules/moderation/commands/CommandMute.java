package com.basketbandit.core.modules.moderation.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandMute extends Command {

    public CommandMute() {
        super("mute", "com.basketbandit.core.modules.moderation.ModuleModeration", 1, new String[]{"-mute @user", "-ban @user [reason]"}, Permission.VOICE_MUTE_OTHERS);
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
            EmbedBuilder embed = new EmbedBuilder().setAuthor("That user could not found.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        e.getGuild().getController().addSingleRoleToMember(target, Utils.setupMutedRole(e.getGuild())).queue();

        if(commandParameters.length < 2) {
            EmbedBuilder embed = new EmbedBuilder().setAuthor(target.getAsMention() + " has been muted.");
            MessageHandler.sendMessage(e, embed.build());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setAuthor(target.getAsMention() + " has been muted for reason: " + commandParameters[1]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
