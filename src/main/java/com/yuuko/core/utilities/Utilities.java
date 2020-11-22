package com.yuuko.core.utilities;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Config;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.metrics.MetricsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;

import java.util.List;

public final class Utilities {

    /**
     * Updates stats for DiscordBotList
     */
    public static void updateDiscordBotList(int shardId) {
        final int shardCount = Config.SHARDS_TOTAL;
        final int guildCount = MetricsManager.getDiscordMetrics(shardId).GUILD_COUNT.get();

        try {
            if(Config.BOT_LIST != null) {
                Config.BOT_LIST.setStats(shardId, shardCount, guildCount);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the server custom prefix.
     * @param guild {@link Guild}
     * @return String
     */
    public static String getServerPrefix(Guild guild) {
        return GuildFunctions.getGuildSetting("prefix", guild.getId());
    }

    /**
     * Returns a pretty version of a command's permission array by removing the brackets surrounding them.
     * @param permissions {@link List<Permission>}
     * @return String
     */
    public static String getCommandPermissions(List<Permission> permissions) {
        return permissions.toString().replace("[", "").replace("]", "");
    }

    /**
     * Returns the specific shard's SelfUser object.
     * @return {@link SelfUser}
     */
    public static SelfUser getSelfUser() {
        for(JDA shard : Config.SHARD_MANAGER.getShards()) {
            if(shard.getStatus().equals(JDA.Status.CONNECTED)) {
                return shard.getSelfUser();
            }
        }
        return null;
    }

    /**
     * Returns YouTube image url or placeholder if it doesn't exist
     * @param track {@link AudioTrack}
     * @return string image url
     */
    public static String getAudioTrackImage(AudioTrack track) {
        String[] uri = track.getInfo().uri.split("=");
        return (uri.length > 1) ? "https://img.youtube.com/vi/" + uri[1] + "/1.jpg" : "https://i.imgur.com/bCNQlm6.jpg";
    }
}
