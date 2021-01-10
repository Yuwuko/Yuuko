// Program: Yuuko (Discord Bot)
// Programmer: Joshua Mark Hunt

package com.yuuko.core;

import com.yuuko.core.commands.audio.handlers.AudioManager;
import com.yuuko.core.database.function.ShardFunctions;
import lavalink.client.io.Link;

public class Yuuko {
    public static void main(String[] args) {
        new Config().setup();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            AudioManager.LAVALINK.getLavalink().getLinks().forEach(Link::destroy);
            Config.SHARD_MANAGER.shutdown();
            Config.SHARD_IDS.forEach(ShardFunctions::updateShardShutdown);
        }));
    }
}