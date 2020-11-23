package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.setting.commands.ModerationLogSetting;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick", Config.MODULES.get("moderation"),1, -1L, Arrays.asList("-kick @user", "-kick @user <reason>"), false, Arrays.asList(Permission.KICK_MEMBERS));
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] params = e.getParameters().split("\\s+", 2);
        Member target = MessageUtilities.getMentionedMember(e, true);

        if(target == null) {
            return;
        }

        if(!Sanitiser.canInteract(e, target, "kick", true)) {
            return;
        }

        if(params.length < 2) {
            e.getGuild().kick(target).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Kick").setDescription(target.getEffectiveName() + " has been successfully kicked.");
                MessageHandler.reply(e, embed.build());
                ModerationLogSetting.execute(e, "Kick", target.getUser(), "None");
            }, f -> e.getMessage().addReaction("❌").queue());
        } else {
            e.getGuild().kick(target, params[1]).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Mute").setDescription(target.getEffectiveName() + " has been successfully muted, for reason: " + params[1] + ".");
                MessageHandler.reply(e, embed.build());
                ModerationLogSetting.execute(e, "Kick", target.getUser(), params[1]);
            }, f -> e.getMessage().addReaction("❌").queue());
        }
    }

}
