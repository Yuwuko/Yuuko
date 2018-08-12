package com.basketbandit.core.utils;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.managers.GuildController;
import org.discordbots.api.client.DiscordBotListAPI;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;

public class Utils {

    public static User botUser;
    public static List<Command> commandList;
    public static List<Module> moduleList;
    public static String commandCount;
    public static DiscordBotListAPI botList;

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
    public static String getTag(User user) {
        return user.getName() + "#" + user.getDiscriminator();
    }

    /**
     * Sends a message, saving those precious bytes.
     * @param event GenericMessageEvent
     * @param message String
     */
    public static void sendMessage(GenericMessageEvent event, String message) {
        event.getTextChannel().sendMessage(message).queue();
    }

    /**
     * Sends an embedded message.
     * @param event GenericMessageEvent
     * @param message MessageEmbed
     */
    public static void sendMessage(GenericMessageEvent event, MessageEmbed message) {
        event.getTextChannel().sendMessage(message).queue();
    }

    /**
     * Sends a message via message channel.
     * @param channel MessageChannel
     * @param message String
     */
    public static void sendMessage(MessageChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

    /**
     * Sends an embedded message via message channel.
     * @param channel MessageChannel
     * @param message MessageEmbed
     */
    public static void sendMessage(MessageChannel channel, MessageEmbed message) {
        channel.sendMessage(message).queue();
    }

    /**
     * Replaces the last occurrence of a pattern with nothing.
     * @param stringBuilder StringBuilder
     * @param pattern String
     * @return StringBuilder
     */
    public static StringBuilder removeLastOccurrence(StringBuilder stringBuilder, String pattern) {
        int index = stringBuilder.lastIndexOf(pattern);
        if(index > -1) {
            stringBuilder.replace(index, index + 1, "");
        }

        return stringBuilder;
    }

    /**
     * Replaces the last occurrence of a pattern with nothing.
     * @param stringBuffer StringBuilder
     * @param pattern String
     * @return StringBuilder
     */
    public static StringBuffer removeLastOccurrence(StringBuffer stringBuffer, String pattern) {
        int index = stringBuffer.lastIndexOf(pattern);
        if(index > -1) {
            stringBuffer.replace(index, index + 1, "");
        }

        return stringBuffer;
    }

    /**
     * Extracts the module name from a class path.
     * @param string String
     * @param shortened boolean
     * @return String
     */
    public static String extractModuleName(String string, boolean shortened, boolean lowercase) {
        String returnString;

        if(shortened) {
            returnString = string.substring(string.lastIndexOf(".") + 7);
        } else {
            returnString = string.substring(string.lastIndexOf(".") + 1);
        }

        if(lowercase) {
            return returnString.toLowerCase();
        } else {
            return returnString;
        }
    }

    /**
     * Creates the muted role to correctly mute people.
     * @param guild Guild
     * @return Role
     */
    public static Role setupMutedRole(Guild guild) {
        GuildController controller = guild.getController();
        List<TextChannel> channels = guild.getTextChannels();
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
                channel.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_SPEAK).complete();
            }
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
                channel.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_SPEAK).complete();
            }
        }

        return muted;
    }

    /**
     * Updates stats for DiscordBotList
     */
    public static void updateDiscordBotList() {
        try {
            JDA jda = botUser.getJDA();
            botList.setStats(botUser.getId(), Math.toIntExact(jda.getGuildCache().size()), jda.getShardInfo().getShardId(), jda.getShardInfo().getShardTotal());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes an input URL (which hopefully contains a JSON response, buffers the response and returns a string.
     * This string can be object mapped into something usable in Java.
     */
    public static String bufferJson(String inputUrl) {
        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {

            URL url = new URL(inputUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if(conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            byte[] buffer = new byte[1024];
            int length;

            while((length = conn.getInputStream().read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString();

        } catch(Exception ex) {
            return "";
        }
    }

}
