package com.yuuko.core.utils;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.Statistics;
import com.yuuko.core.database.DatabaseFunctions;
import net.dv8tion.jda.core.EmbedBuilder;
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
     * Replaces the last occurrence of a pattern with nothing.
     * @param stringBuilder StringBuilder
     * @param pattern String
     * @return StringBuilder
     */
    public static void removeLastOccurrence(StringBuilder stringBuilder, String pattern) {
        int index = stringBuilder.lastIndexOf(pattern);
        if(index > -1) {
            stringBuilder.replace(index, index + 1, "");
        }
    }

    /**
     * Replaces the last occurrence of a pattern with nothing.
     * @param stringBuffer StringBuilder
     * @param pattern String
     * @return StringBuilder
     */
    public static void removeLastOccurrence(StringBuffer stringBuffer, String pattern) {
        int index = stringBuffer.lastIndexOf(pattern);
        if(index > -1) {
            stringBuffer.replace(index, index + 1, "");
        }
    }

    /**
     * Extracts the module name from a class path.
     * @param string String
     * @param shortened boolean
     * @return String
     */
    public static String extractModuleName(String string, boolean shortened, boolean lowercase) {
        String returnString = (shortened) ? string.substring(string.lastIndexOf(".") + 7) : string.substring(string.lastIndexOf(".") + 1);
        return (lowercase) ? returnString.toLowerCase() : returnString;
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
     * Console output message method.
     */
    public static void consoleOutput() {
        // Default shell size is 80x24, this output will allow each message to take up the whole screen
        // thus giving the illusion that the current page is changing rather than just being rewritten under.
        System.out.println();
        System.out.println("            __   __           _          __   __    _   ___   ___");
        System.out.println("            \\ \\ / /   _ _   _| | _____   \\ \\ / /   / | / _ \\ / _ \\ ");
        System.out.println("             \\ V / | | | | | | |/ / _ \\   \\ V /____| || | | | | | | ");
        System.out.println("              | || |_| | |_| |   < (_) |   | |_____| || |_| | |_| | ");
        System.out.println("              |_| \\__,_|\\__,_|_|\\_\\___/    |_|     |_(_)___(_)___/ ");
        System.out.println();
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[COMMANDS " + Cache.MODULES.size() + "/" + Cache.COMMANDS.size() + "]━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(0));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(1));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(2));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(3));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(4));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(5));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(6));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(7));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(8));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(9));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(10));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(11));
        System.out.println("┃ " + Cache.LAST_THIRTEEN.get(12));
        System.out.println("┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[STATISTICS]━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫");
        System.out.println("┃ Uptime: " + Statistics.RUNTIME + ", Ping: " + Statistics.PING + ", DB Idle: " + Statistics.DB_POOL_IDLE + ", DB Active: " + Statistics.DB_POOL_ACTIVE);
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    /**
     * Updates the latest [INFO] message or the latest command message.
     * @param latest String
     */
    public static void updateLatest(String latest) {
        if(latest.startsWith("[INFO]")) {
            Cache.LATEST_INFO = latest;
        } else {
            Cache.LAST_THIRTEEN.addFirst(latest);
            if(Cache.LAST_THIRTEEN.size() > 12) {
                Cache.LAST_THIRTEEN.removeLast();
            }
        }
    }

    /**
     * Gets current songs timeStamp.
     * @param milliseconds; how many milliseconds of the song has played.
     * @return formatted timeStamp.
     */
    public static String getTimestamp(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        int days    = (int) ((milliseconds / (1000 * 60 * 60 * 24)));

        if(days > 0) {
            return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        } else if(hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
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
        return new DatabaseFunctions().getServerSetting("commandPrefix", server);
    }

    /**
     * Returns the first mentioned user from a given message.
     * @param e MessageReceivedEvent
     * @return Member
     */
    public static Member getMentionedMember(MessageReceivedEvent e) {
        List<Member> mentioned = e.getMessage().getMentionedMembers();

        if(!e.getMessage().mentionsEveryone()) {
            if(mentioned.size() > 0) {
                return mentioned.get(0);
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("There were no mentioned users found.");
                MessageHandler.sendMessage(e, embed.build());
                return null;
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameters").setDescription("You cannot do _that_ to everyone.");
            MessageHandler.sendMessage(e, embed.build());
            return null;
        }
    }

    /**
     * Returns a pretty version of a command's permission array by removing the brackets surrounding them.
     * @param permissions Permission[]
     * @return String
     */
    public static String getCommandPermissions(Permission[] permissions) {
        return Arrays.toString(permissions).replace("[", "").replace("]", "");
    }

}
