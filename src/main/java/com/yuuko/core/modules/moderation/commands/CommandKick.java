package com.yuuko.core.modules.moderation.commands;

import com.yuuko.core.modules.Command;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandKick extends Command {

    public CommandKick() {
        super("kick", "com.yuuko.core.modules.moderation.ModuleModeration",1, new String[]{"-kick @user", "-ban @user [reason]"}, new Permission[]{Permission.KICK_MEMBERS});
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 3);
        Member target = Utils.getMentionedMember(e);

        if(target == null) {
            return;
        }

        if(commandParameters.length < 3) {
            e.getGuild().getController().kick(target).queue();
        } else {
            e.getGuild().getController().kick(target, commandParameters[1]).queue();
        }
    }

}
