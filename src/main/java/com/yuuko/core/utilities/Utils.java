package com.yuuko.core.utilities;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.DatabaseFunctions;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.Arrays;
import java.util.List;

public final class Utils {

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
            Configuration.BOT_LIST.setStats(Configuration.BOT.getJDA().getShardInfo().getShardId(), Configuration.BOT.getJDA().getShardInfo().getShardTotal(), Math.toIntExact(Configuration.BOT.getJDA().getGuildCache().size()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an API ApplicationId.
     *
     * @param name name of the api
     * @return String
     */
    public static String getApiApplicationId(String name) {
        return Configuration.API_KEYS.get(name).getApplicationId();
    }

    /**
     * Returns an API key.
     *
     * @param name name of the api
     * @return String
     */
    public static String getApiKey(String name) {
        return Configuration.API_KEYS.get(name).getKey();
    }

    /**
     * Returns the server custom prefix.
     *
     * @param server the server to retrieve the prefix from
     * @return String
     */
    public static String getServerPrefix(String server) {
        return DatabaseFunctions.getGuildSetting("prefix", server);
    }

    /**
     * Returns a pretty version of a command's permission array by removing the brackets surrounding them.
     *
     * @param permissions Permission[]
     * @return String
     */
    public static String getCommandPermissions(Permission[] permissions) {
        return Arrays.toString(permissions).replace("[", "").replace("]", "");
    }

    /**
     * Checks to see if a text channel is nsfw or not.
     *
     * @param e MessageReceivedEvent
     * @return boolean
     */
    public static boolean isChannelNSFW(MessageReceivedEvent e) {
        return e.getTextChannel().isNSFW();
    }

    /**
     * Returns the specific shard's SelfUser object.
     *
     * @return SelfUser
     */
    public static SelfUser getSelfUser() {
        for(JDA shard : Configuration.SHARD_MANAGER.getShards()) {
            if(shard.getStatus().equals(JDA.Status.CONNECTED)) {
                return shard.getSelfUser();
            }
        }
        return null;
    }

    /**
     * Retrieves a command from the command list by name.
     *
     * @param name the name of the command to get.
     * @return Command
     */
    public static Command getCommandByName(String name) {
        for(Command command: Configuration.COMMANDS) {
            if(command.getName().equals(name)) {
                return command;
            }
        }
        return null;
    }

    /**
     * Gets the module name of a module via the class simple name.
     *
     * @param module the module class to extract the name from.
     * @return String
     */
    public static String getModuleName(Class<?> module) {
        return module.getSimpleName().replace("Module", "");
    }
}
