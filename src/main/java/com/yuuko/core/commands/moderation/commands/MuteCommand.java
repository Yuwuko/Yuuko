package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.moderation.ModerationModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute", ModerationModule.class, 1, new String[]{"-mute @user", "-ban @user [reason]"}, false, new Permission[]{Permission.VOICE_MUTE_OTHERS});
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 3);
        Member target = MessageUtility.getFirstMentionedMember(e);

        if(target == null) {
            return;
        }

        if(target.getRoles().stream().noneMatch((role) -> role.getName().equalsIgnoreCase("Muted"))) {
            e.getGuild().getController().addSingleRoleToMember(target, Utils.setupMutedRole(e.getGuild())).queue((r) -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Mute")
                        .setDescription("**" + target.getEffectiveName()  + "** has been successfully muted.")
                        .addField("Moderator", e.getMessage().getMember().getEffectiveName(), true)
                        .addField("Reason", (commandParameters.length < 2) ? "None" : commandParameters[1], true);
                MessageHandler.sendMessage(e, embed.build());
            }, (r) -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Mute").setDescription("Muting of **" + target.getEffectiveName()  + "** was unsuccessful.");
                MessageHandler.sendMessage(e, embed.build());
            });
        } else {
            e.getGuild().getController().removeSingleRoleFromMember(target, Utils.setupMutedRole(e.getGuild())).queue((r) -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Unmute").setDescription("**" + target.getEffectiveName()  + "** has been successfully unmuted.");
                MessageHandler.sendMessage(e, embed.build());
            }, (r) -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Unmute").setDescription("Unmuting of **" + target.getEffectiveName() + "** was unsuccessful.");
                MessageHandler.sendMessage(e, embed.build());
            });
        }
    }

}
