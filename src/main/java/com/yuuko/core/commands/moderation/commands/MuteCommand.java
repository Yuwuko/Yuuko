package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.setting.commands.ModerationLogSetting;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute", Yuuko.MODULES.get("moderation"), 1, -1L, Arrays.asList("-mute @user", "-mute @user <reason>"), false, Arrays.asList(Permission.VOICE_MUTE_OTHERS));
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        String[] params = e.getParameters().split("\\s+", 3);
        Member target = MessageUtilities.getMentionedMember(e, true);

        if(target == null) {
            return;
        }

        if(!Sanitiser.canInteract(e, target, "mute", true)) {
            return;
        }

        Role muted = getMutedRole(e.getGuild());
        if(muted == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Mute (Setup)").setDescription("Unable to successfully set up `mute` role. Either the role is equal/higher on the hierarchy to/than me, or the server has the maximum (250) number of roles.");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        Consumer<Throwable> failure = f -> e.getMessage().addReaction("❌").queue();
        if(target.getRoles().stream().noneMatch((role) -> role.getName().equalsIgnoreCase("Muted"))) {
            e.getGuild().addRoleToMember(target, muted).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Mute").setDescription(target.getEffectiveName() + " has been successfully muted.");
                MessageDispatcher.reply(e, embed.build());
                ModerationLogSetting.execute(e, "Mute", target.getUser(), (params.length < 2) ? "None" : params[1]);
            }, failure);
        } else {
            e.getGuild().removeRoleFromMember(target, muted).queue(s -> {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Unmute").setDescription(target.getEffectiveName() + " has been successfully unmuted.");
                MessageDispatcher.reply(e, embed.build());
                ModerationLogSetting.execute(e, "Unmute", target.getUser(), (params.length < 2) ? "None" : params[1]);
            }, failure);
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
