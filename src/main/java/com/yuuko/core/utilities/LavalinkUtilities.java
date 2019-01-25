package com.yuuko.core.utilities;

import com.yuuko.core.Cache;
import lavalink.client.io.Link;
import net.dv8tion.jda.core.entities.Guild;

public class LavalinkUtilities {

    public static boolean isState(Guild guild, Link.State state) {
        return Cache.LAVALINK.getLavalink().getLink(guild).getState() == state;
    }
}
