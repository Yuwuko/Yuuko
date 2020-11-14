package com.yuuko.core.commands.audio.handlers.lavalink;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.audio.handlers.lavalink.entity.LavalinkNode;
import lavalink.client.io.jda.JdaLavalink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class LavalinkManager {
    private static final Logger log = LoggerFactory.getLogger(LavalinkManager.class);
    private final JdaLavalink lavalink;

    public LavalinkManager() {
        lavalink = new JdaLavalink(
                Configuration.BOT_ID,
                Configuration.SHARD_COUNT,
                shardId -> Configuration.SHARD_MANAGER.getShardById(shardId)
        );
        lavalink.setAutoReconnect(true);

        try(InputStream inputStream = new FileInputStream(new File("./config/lavalink.yaml"))) {
            Yaml yaml = new Yaml(new Constructor(LavalinkNode.class));
            for(Object object : yaml.loadAll(inputStream)) {
                LavalinkNode node = (LavalinkNode) object;
                lavalink.addNode(node.getURI(), node.getPassword());
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }

    public JdaLavalink getLavalink() {
        return lavalink;
    }

}
