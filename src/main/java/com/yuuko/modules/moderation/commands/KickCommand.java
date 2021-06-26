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

public class KickCommand extends Command {

    public KickCommand() {
        super("kick", Arrays.asList("-kick @user", "-kick @user <reason>"), Arrays.asList(Permission.KICK_MEMBERS), 1);
    }

    @Override
    public void onCommand(MessageEvent context) {
        String[] params = context.getParameters().split("\\s+", 2);
        Member target = MessageUtilities.getMentionedMember(context, true);

        if(target == null || !Sanitiser.canInteract(context, target, "kick", true)) {
            return;
        }

        if(params.length < 2) {
            context.getGuild().kick(target).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder().
                        setTitle("Kick")
                        .setDescription(context.i18n("success").formatted(target.getEffectiveName()));
                MessageDispatcher.reply(context, embed.build());
                ModerationLogSetting.execute(context, target.getUser(), context.i18n("no_reason"));
            }, f -> context.getMessage().addReaction("❌").queue());
        } else {
            context.getGuild().kick(target, params[1]).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("action"))
                        .setDescription(context.i18n("success_reason").formatted(target.getEffectiveName(),params[1]));
                MessageDispatcher.reply(context, embed.build());
                ModerationLogSetting.execute(context, target.getUser(), params[1]);
            }, f -> context.getMessage().addReaction("❌").queue());
        }
    }

}
