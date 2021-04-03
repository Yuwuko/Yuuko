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
        super("kick",1, -1L, Arrays.asList("-kick @user", "-kick @user <reason>"), false, Arrays.asList(Permission.KICK_MEMBERS));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        String[] params = context.getParameters().split("\\s+", 2);
        Member target = MessageUtilities.getMentionedMember(context, true);

        if(target == null) {
            return;
        }

        if(!Sanitiser.canInteract(context, target, "kick", true)) {
            return;
        }

        if(params.length < 2) {
            context.getGuild().kick(target).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Kick").setDescription(target.getEffectiveName() + " has been successfully kicked.");
                MessageDispatcher.reply(context, embed.build());
                ModerationLogSetting.execute(context, "Kick", target.getUser(), "None");
            }, f -> context.getMessage().addReaction("❌").queue());
        } else {
            context.getGuild().kick(target, params[1]).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Mute").setDescription(target.getEffectiveName() + " has been successfully muted, for reason: " + params[1] + ".");
                MessageDispatcher.reply(context, embed.build());
                ModerationLogSetting.execute(context, "Kick", target.getUser(), params[1]);
            }, f -> context.getMessage().addReaction("❌").queue());
        }
    }

}
