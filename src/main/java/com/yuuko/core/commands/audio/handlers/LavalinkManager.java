package com.yuuko.core.commands.audio.handlers;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import lavalink.client.io.jda.JdaLavalink;
import lavalink.client.player.IPlayer;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;

public class LavalinkManager {

    private JdaLavalink lavalink;

    public LavalinkManager() {
        lavalink = new JdaLavalink(
                Configuration.BOT_ID,
                Configuration.SHARD_COUNT,
                shardId -> Cache.SHARD_MANAGER.getShardById(shardId)
        );
        lavalink.setAutoReconnect(true);

        try {
            BufferedReader s = new BufferedReader(new FileReader("lavalink_configuration.txt"));
            lavalink.addNode(URI.create(s.readLine()), s.readLine());
            s.close();
        } catch(Exception ex) {
            //
        }
    }

    public JdaLavalink getLavalink() {
        return lavalink;
    }

    IPlayer createPlayer(String guildId) {
        return lavalink.getLink(guildId).getPlayer();
    }

    public void openConnection(VoiceChannel channel) {
        lavalink.getLink(channel.getGuild()).connect(channel);
    }

    public void closeConnection(Guild guild) {
        lavalink.getLink(guild).disconnect();
    }
}
