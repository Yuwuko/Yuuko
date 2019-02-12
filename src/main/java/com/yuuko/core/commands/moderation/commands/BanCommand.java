package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.ModerationLogSetting;
import com.yuuko.core.commands.moderation.ModerationModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtilities;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class BanCommand extends Command {

    public BanCommand() {
        super("ban", ModerationModule.class,1, new String[]{"-ban @user", "-ban @user [delDays]", "-ban @user [reason]", "-ban @user [delDays] [reason]"}, false, new Permission[]{Permission.BAN_MEMBERS});
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 3);
        Member target = MessageUtilities.getMentionedMember(e, commandParameters, true);

        if(target == null) {
            return;
        }

        if(!Sanitiser.canInteract(e, target, "ban", true)) {
            return;
        }

        if(commandParameters.length == 1) { // Case: Ban w/o reason and delDays.
            e.getGuild().getController().ban(target, 0).queue(s -> {
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, target.getUser(), 0, "None");
            }, f -> e.getMessage().addReaction("❌").queue());
            return;
        }

        AtomicInteger delDays = new AtomicInteger();

        if(Sanitiser.isNumber(commandParameters[1])) {
            delDays.set(Integer.parseInt(commandParameters[1]));
            if(delDays.get() > 7) {
                delDays.set(7);
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Deletion days must be no larger than 7. You input `**" + commandParameters[1] + "**`, so the actual value has been capped at 7.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            e.getGuild().getController().ban(target, 0, commandParameters[1]).queue(s -> { // Case: Ban w/reason, but no delDays.
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, target.getUser(), 0, commandParameters[1]);
            }, f -> e.getMessage().addReaction("❌").queue());
            return;
        }

        if(commandParameters.length < 3) {
            e.getGuild().getController().ban(target, delDays.get()).queue(s -> { // Case: Ban w/delDays, but no reason.
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, target.getUser(), delDays.get(), "None");
            }, f -> e.getMessage().addReaction("❌").queue());
        } else {
            e.getGuild().getController().ban(target, delDays.get(), commandParameters[2]).queue(s -> { // Case: Ban w/reason and delDays.
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, target.getUser(), delDays.get(), commandParameters[2]);
            }, f -> e.getMessage().addReaction("❌").queue());
        }
    }

}
