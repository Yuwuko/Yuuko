package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.ModerationLogSetting;
import com.yuuko.core.commands.moderation.ModerationModule;
import com.yuuko.core.utilities.MessageUtilities;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute", ModerationModule.class, 1, new String[]{"-mute @user", "-mute @user [reason]"}, false, new Permission[]{Permission.VOICE_MUTE_OTHERS});
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 3);
        Member target;

        if(commandParameters[0].length() == 18 && Sanitiser.isNumber(commandParameters[0])) {
            target = e.getGuild().getMemberById(commandParameters[0]);
        } else {
            target = MessageUtilities.getFirstMentionedMember(e);
        }

        if(target == null) {
            return;
        }

        if(target.getRoles().stream().noneMatch((role) -> role.getName().equalsIgnoreCase("Muted"))) {
            e.getGuild().getController().addSingleRoleToMember(target, Utils.setupMutedRole(e.getGuild())).queue(s -> {
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, "Mute", target.getUser(), (commandParameters.length < 2) ? "None" : commandParameters[1]);
            }, f -> {
                e.getMessage().addReaction("❌").queue();
            });
        } else {
            e.getGuild().getController().removeSingleRoleFromMember(target, Utils.setupMutedRole(e.getGuild())).queue(s -> {
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, "Unmute", target.getUser(), (commandParameters.length < 2) ? "None" : commandParameters[1]);
            }, f -> {
                e.getMessage().addReaction("❌").queue();
            });
        }
    }

}
