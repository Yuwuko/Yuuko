package com.yuuko.core.utilities;

import com.yuuko.core.Configuration;
import lavalink.client.io.Link;
import net.dv8tion.jda.core.entities.Guild;

public class LavalinkUtilities {

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
}
