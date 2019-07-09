package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.ModerationLogSetting;
import com.yuuko.core.commands.moderation.ModerationModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.DiscordUtilities;
import com.yuuko.core.utilities.MessageUtilities;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.Arrays;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute", ModerationModule.class, 1, Arrays.asList("-mute @user", "-mute @user <reason>"), false, Arrays.asList(Permission.VOICE_MUTE_OTHERS));
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] commandParameters = e.getParameters().split("\\s+", 3);
        Member target = MessageUtilities.getMentionedMember(e, true);

        if(target == null) {
            return;
        }

        if(!Sanitiser.canInteract(e, target, "mute", true)) {
            return;
        }

        Role muted = DiscordUtilities.getMutedRole(e.getGuild());
        if(muted == null) {
            return;
        }

        if(target.getRoles().stream().noneMatch((role) -> role.getName().equalsIgnoreCase("Muted"))) {
            e.getGuild().getController().addSingleRoleToMember(target, muted).queue(s -> {
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, "Mute", target.getUser(), (commandParameters.length < 2) ? "None" : commandParameters[1]);
            }, f -> e.getMessage().addReaction("❌").queue());
        } else {
            e.getGuild().getController().removeSingleRoleFromMember(target, muted).queue(s -> {
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, "Unmute", target.getUser(), (commandParameters.length < 2) ? "None" : commandParameters[1]);
            }, f -> e.getMessage().addReaction("❌").queue());
        }
    }

}
