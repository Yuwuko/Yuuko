package com.yuuko.core.utilities;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;

public final class DiscordUtilities {


    /**
     * Returns a username and discriminator in format username#discriminator.
     *
     * @param member the member to retrieve
     * @return username#discriminator
     */
    public static String getTag(Member member) {
        return getTag(member.getUser());
    }

    /**
     * Returns a username and discriminator in format username#discriminator.
     *
     * @param user the user to retrieve
     * @return username#discriminator
     */
    private static String getTag(User user) {
        return user.getName() + "#" + user.getDiscriminator();
    }

    /**
     * Creates the muted role to correctly mute people.
     *
     * @param guild Guild
     * @return Role
     */
    public static Role getMutedRole(Guild guild) {
        List<TextChannel> channels = guild.getTextChannels();
        List<VoiceChannel> voiceChannels = guild.getVoiceChannels();
        List<Role> roleList = guild.getRoles();
        Role muted = null;

        for(Role role: roleList) {
            if(role.getName().equals("Muted")) {
                muted = role;
                break;
            }
        }

        if(muted == null) {
            if(!guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
                return null;
            }

            muted = guild.createRole().setName("Muted").complete();

            for(TextChannel channel: channels) {
                channel.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).queue();
            }

            for(VoiceChannel channel: voiceChannels) {
                channel.createPermissionOverride(muted).setDeny(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).queue();
            }
        }

        return muted;
    }
}
