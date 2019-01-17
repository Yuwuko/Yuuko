package com.yuuko.core.utilities;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.database.DatabaseFunctions;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class Utils {

    /**
     * Returns a username and discriminator in format username#discriminator.
     * @param member the member to retrieve
     * @return username#discriminator
     */
    public static String getTag(Member member) {
        return getTag(member.getUser());
    }

    /**
     * Returns a username and discriminator in format username#discriminator.
     * @param user the user to retrieve
     * @return username#discriminator
     */
    private static String getTag(User user) {
        return user.getName() + "#" + user.getDiscriminator();
    }



    /**
     * Creates the muted role to correctly mute people.
     * @param guild Guild
     * @return Role
     */
    public static Role setupMutedRole(Guild guild) {
        GuildController controller = guild.getController();
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
            muted = controller.createRole().setName("Muted").setPermissions(Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY, Permission.VOICE_CONNECT).complete();

            for(TextChannel channel: channels) {
                channel.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).complete();
            }

            for(VoiceChannel channel: voiceChannels) {
                channel.createPermissionOverride(muted).setDeny(Permission.VOICE_SPEAK).complete();
            }


            for(TextChannel channel: channels) {
                PermissionOverride override = null;

                for(PermissionOverride channelOverride: channel.getRolePermissionOverrides()) {
                    if(channelOverride.getRole().equals(muted)) {
                        override = channelOverride;
                        break;
                    }
                }

                if(override == null) {
                    channel.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD).complete();
                }
            }

        }

        return muted;
    }

    /**
     * Updates stats for DiscordBotList
     */
    public static void updateDiscordBotList() {
        try {
            Cache.BOT_LIST.setStats(Cache.JDA.getShardInfo().getShardId(), Cache.JDA.getShardInfo().getShardTotal(), Math.toIntExact(Cache.JDA.getGuildCache().size()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes a path and encoding type and returns a string of the file.
     * @param path String
     * @param encoding Charset
     * @return String retrieved from file.
     */
    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    /**
     * Updates the latest [INFO] message or the latest command message.
     * @param latest String
     */
    public static void updateLatest(String latest) {
        if(latest.startsWith("[INFO]")) {
            Cache.LATEST_INFO = latest;
        }
    }

    /**
     * Returns an API ApplicationId.
     * @param name name of the api
     * @return String
     */
    public static String getApiApplicationId(String name) {
        return Configuration.API_KEYS.get(name).getApplicationId();
    }

    /**
     * Returns an API key.
     * @param name name of the api
     * @return String
     */
    public static String getApiKey(String name) {
        return Configuration.API_KEYS.get(name).getKey();
    }

    /**
     * Returns the server custom prefix.
     * @param server the server to retrieve the prefix from
     * @return String
     */
    public static String getServerPrefix(String server) {
        return new DatabaseFunctions().getGuildSetting("commandPrefix", server);
    }

    /**
     * Returns a pretty version of a command's permission array by removing the brackets surrounding them.
     * @param permissions Permission[]
     * @return String
     */
    public static String getCommandPermissions(Permission[] permissions) {
        return Arrays.toString(permissions).replace("[", "").replace("]", "");
    }

    /**
     * Returns if a text channel is nsfw or not.
     * @param e MessageReceivedEvent
     * @return boolean
     */
    public static boolean isChannelNSFW(MessageReceivedEvent e) {
        return e.getTextChannel().isNSFW();
    }

}
