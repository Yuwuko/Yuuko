package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.setting.commands.ModerationLogSetting;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.DiscordUtilities;
import com.yuuko.core.utilities.MessageUtilities;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.Arrays;
import java.util.function.Consumer;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute", Config.MODULES.get("moderation"), 1, -1L, Arrays.asList("-mute @user", "-mute @user <reason>"), false, Arrays.asList(Permission.VOICE_MUTE_OTHERS));
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] params = e.getParameters().split("\\s+", 3);
        Member target = MessageUtilities.getMentionedMember(e, true);

        if(target == null) {
            return;
        }

        if(!Sanitiser.canInteract(e, target, "mute", true)) {
            return;
        }

        Role muted = DiscordUtilities.getOrSetupMutedRole(e.getGuild());
        if(muted == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Mute (Setup)").setDescription("Unable to successfully set up `mute` role. Either the role is equal/higher on the hierarchy to/than me, or the server has the maximum (250) number of roles.");
            MessageHandler.reply(e, embed.build());
            return;
        }

        Consumer<Throwable> failure = f -> e.getMessage().addReaction("❌").queue();
        if(target.getRoles().stream().noneMatch((role) -> role.getName().equalsIgnoreCase("Muted"))) {
            e.getGuild().addRoleToMember(target, muted).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Mute").setDescription(target.getEffectiveName() + " has been successfully muted.");
                MessageHandler.reply(e, embed.build());
                ModerationLogSetting.execute(e, "Mute", target.getUser(), (params.length < 2) ? "None" : params[1]);
            }, failure);
        } else {
            e.getGuild().removeRoleFromMember(target, muted).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Unmute").setDescription(target.getEffectiveName() + " has been successfully unmuted.");
                MessageHandler.reply(e, embed.build());
                ModerationLogSetting.execute(e, "Unmute", target.getUser(), (params.length < 2) ? "None" : params[1]);
            }, failure);
        }
    }

}
