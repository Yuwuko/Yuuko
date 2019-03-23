package com.yuuko.core.utilities;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.GuildFunctions;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

public final class Utilities {


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
     * @param guild the server to retrieve the prefix from
     * @return String
     */
    public static String getServerPrefix(Guild guild) {
        return GuildFunctions.getGuildSetting("prefix", guild.getId());
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
