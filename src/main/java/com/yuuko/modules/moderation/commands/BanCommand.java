package com.yuuko.modules.moderation.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.setting.commands.ModerationLogSetting;
import com.yuuko.utilities.MessageUtilities;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class BanCommand extends Command {

    public BanCommand() {
        super("ban", Arrays.asList("-ban @user", "-ban @user <delDays>", "-ban @user <reason>", "-ban @user <delDays> <reason>"), Arrays.asList(Permission.BAN_MEMBERS), 1);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        String[] commandParameters = context.getParameters().split("\\s+", 3);
        Member target = MessageUtilities.getMentionedMember(context, true);

        if(target == null || !Sanitiser.canInteract(context, target, "ban", true)) {
            return;
        }

        if(commandParameters.length == 1) { // Case: Ban w/o reason and delDays.
            context.getGuild().ban(target, 0).queue(s -> {
                context.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(context, target.getUser(), context.i18n("no_reason"));
            }, f -> context.getMessage().addReaction("❌").queue());
            return;
        }

        AtomicInteger delDays = new AtomicInteger();

        if(Sanitiser.isNumeric(commandParameters[1])) {
            delDays.set(Integer.parseInt(commandParameters[1]));
            if(delDays.get() > 7) {
                delDays.set(7);
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("bad_input"))
                        .setDescription(context.i18n("bad_input_desc").formatted(commandParameters[1]));
                MessageDispatcher.reply(context, embed.build());
            }
        } else {
            context.getGuild().ban(target, 0, commandParameters[1]).queue(s -> { // Case: Ban w/reason, but no delDays.
                context.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(context, target.getUser(), commandParameters[1]);
            }, f -> context.getMessage().addReaction("❌").queue());
            return;
        }

        if(commandParameters.length < 3) {
            context.getGuild().ban(target, delDays.get()).queue(s -> { // Case: Ban w/delDays, but no reason.
                context.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(context, target.getUser(), context.i18n("no_reason"));
            }, f -> context.getMessage().addReaction("❌").queue());
        } else {
            context.getGuild().ban(target, delDays.get(), commandParameters[2]).queue(s -> { // Case: Ban w/reason and delDays.
                context.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(context, target.getUser(), commandParameters[2]);
            }, f -> context.getMessage().addReaction("❌").queue());
        }
    }

}
