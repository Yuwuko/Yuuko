package com.yuuko.modules.moderation.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.setting.commands.ModerationLogSetting;
import com.yuuko.utilities.MessageUtilities;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.Arrays;
import java.util.List;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute", 1, -1L, Arrays.asList("-mute @user", "-mute @user <reason>"), false, Arrays.asList(Permission.VOICE_MUTE_OTHERS));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        String[] params = context.getParameters().split("\\s+", 3);
        Member target = MessageUtilities.getMentionedMember(context, true);
        Role muted; // Set below

        if(target == null || !Sanitiser.canInteract(context, target, "mute", true) || (muted = getMutedRole(context.getGuild())) == null) {
            return;
        }

        if(target.getRoles().stream().noneMatch((role) -> role.getName().equalsIgnoreCase("Muted"))) {
            context.getGuild().addRoleToMember(target, muted).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("action"))
                        .setDescription(context.i18n("success").formatted(target.getEffectiveName()));
                MessageDispatcher.reply(context, embed.build());
                ModerationLogSetting.execute(context, target.getUser(), (params.length < 2) ? "None" : params[1]);
            }, f -> context.getMessage().addReaction("❌").queue());
        } else {
            context.getGuild().removeRoleFromMember(target, muted).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("action"))
                        .setDescription(context.i18n("success_reverse").formatted(target.getEffectiveName()));
                MessageDispatcher.reply(context, embed.build());
                ModerationLogSetting.execute(context, target.getUser(), (params.length < 2) ? "None" : params[1]);
            }, f -> context.getMessage().addReaction("❌").queue());
        }
    }

    /**
     * Creates the muted role to correctly mute people.
     *
     * @param guild {@link Guild}
     * @return Role
     */
    public static Role getMutedRole(Guild guild) {
        List<TextChannel> channels = guild.getTextChannels();
        List<VoiceChannel> voiceChannels = guild.getVoiceChannels();

        for(Role role: guild.getRoles()) {
            if(role.getName().equals("Muted")) {
                if(!guild.getSelfMember().canInteract(role)) {
                    return null;
                }
                return role;
            }
        }

        // max number of roles in guild is 250
        if(!guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES) || guild.getRoleCache().size() == 250) {
            return null;
        }

        Role muted = guild.createRole().setName("Muted").complete();
        for(TextChannel channel: channels) {
            channel.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).queue();
        }
        for(VoiceChannel channel: voiceChannels) {
            channel.createPermissionOverride(muted).setDeny(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).queue();
        }

        return muted;
    }

}
