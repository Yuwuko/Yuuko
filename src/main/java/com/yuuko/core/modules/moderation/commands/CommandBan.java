package com.yuuko.core.modules.moderation.commands;

import com.yuuko.core.modules.Command;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandBan extends Command {

    public CommandBan() {
        super("ban", "com.yuuko.core.modules.moderation.ModuleModeration",1, new String[]{"-ban @user [days]", "-ban @user [days] [reason]"}, new Permission[]{Permission.BAN_MEMBERS});
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 3);
        Member target = Utils.getMentionedMember(e);

        if(target == null) {
            return;
        }

        final int time;

        if(Sanitiser.isNumber(commandParameters[1])) {
            time = Integer.parseInt(commandParameters[1]);
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Time parameter '**" + commandParameters[1] + "**' is invalid, ban defaulted to 1 day.");
            MessageHandler.sendMessage(e, embed.build());
            time = 1;
        }

        if(commandParameters.length < 3) {
            e.getGuild().getController().ban(target, time).queue();
        } else {
            e.getGuild().getController().ban(target, time, commandParameters[2]).queue();
        }
    }

}
