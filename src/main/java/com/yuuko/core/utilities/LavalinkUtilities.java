package com.yuuko.core.utilities;

import com.yuuko.core.Configuration;
import lavalink.client.io.Link;
import lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.entities.Guild;

public final class LavalinkUtilities {

    /**
     * Checks to see if the Lavalink link is the given state.
     *
     * @param guild Guild
     * @param state Link.State
     * @return boolean
     */
    public static boolean isState(Guild guild, Link.State state) {
        return Configuration.LAVALINK.getLavalink().getLink(guild).getState() == state;
    }

    /**
     * Gets the guild's Lavalink player
     *
     * @param guild Guild
     * @return LavalinkPlayer
     */
    public static LavalinkPlayer getPlayer(Guild guild) {
        return Configuration.LAVALINK.getLavalink().getLink(guild).getPlayer();
    }

    /**
     * Gets the currently playing tracks position.
     *
     * @param guild Guild
     * @return long
     */
    public static long getTrackPosition(Guild guild) {
        return getPlayer(guild).getTrackPosition();
    }

}
